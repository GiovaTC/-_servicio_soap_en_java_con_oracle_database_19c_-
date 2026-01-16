package avion;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Publicador {

    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            server.createContext("/AvionService", (HttpExchange exchange) -> {

                /* =========================
                   WSDL
                   ========================= */
                if ("GET".equalsIgnoreCase(exchange.getRequestMethod())
                        && "wsdl".equalsIgnoreCase(exchange.getRequestURI().getQuery())) {

                    byte[] wsdl = Files.readAllBytes(
                            Paths.get("AvionService.wsdl")
                    );

                    exchange.getResponseHeaders().add("Content-Type", "text/xml; charset=utf-8");
                    exchange.sendResponseHeaders(200, wsdl.length);

                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(wsdl);
                    }
                    return;
                }

                /* =========================
                   SOAP POST
                   ========================= */
                if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {

                    InputStream is = exchange.getRequestBody();
                    String xml = new String(is.readAllBytes(), StandardCharsets.UTF_8);

                    // Extraer datos del SOAP
                    String modelo = extraer(xml, "modelo");
                    String fabricante = extraer(xml, "fabricante");
                    int capacidad = Integer.parseInt(extraer(xml, "capacidad"));
                    int autonomia = Integer.parseInt(extraer(xml, "autonomia"));

                    // Persistir en Oracle
                    Avion avion = new Avion(modelo, fabricante, capacidad, autonomia);
                    AvionDAO dao = new AvionDAO();
                    dao.registrarAvion(avion);

                    // Respuesta SOAP válida
                    String response =
                            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                                    "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                                    "<soapenv:Body>" +
                                    "<registrarAvionResponse>" +
                                    "<return>Avión registrado correctamente en Oracle 19c</return>" +
                                    "</registrarAvionResponse>" +
                                    "</soapenv:Body>" +
                                    "</soapenv:Envelope>";

                    byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

                    exchange.getResponseHeaders().add("Content-Type", "text/xml; charset=utf-8");
                    exchange.sendResponseHeaders(200, responseBytes.length);

                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(responseBytes);
                    }
                    return;
                }

                exchange.sendResponseHeaders(405, -1);
            });

            server.start();

            System.out.println("Servicio SOAP activo");
            System.out.println("Endpoint:");
            System.out.println("http://localhost:8080/AvionService");
            System.out.println("WSDL:");
            System.out.println("http://localhost:8080/AvionService?wsdl");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String extraer(String xml, String tag) {
        return xml.substring(
                xml.indexOf("<" + tag + ">") + tag.length() + 2,
                xml.indexOf("</" + tag + ">")
        );
    }
}


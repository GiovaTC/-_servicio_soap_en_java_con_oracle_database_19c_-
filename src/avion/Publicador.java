package avion;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Publicador {

    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            server.createContext("/AvionService", (HttpExchange exchange) -> {

                if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {

                    InputStream is = exchange.getRequestBody();
                    String xml = new String(is.readAllBytes(), StandardCharsets.UTF_8);

                    // Extraer datos simples del SOAP (enfoque académico)
                    String modelo = extraer(xml, "modelo");
                    String fabricante = extraer(xml, "fabricante");
                    int capacidad = Integer.parseInt(extraer(xml, "capacidad"));
                    int autonomia = Integer.parseInt(extraer(xml, "autonomia"));

                    // Persistencia en Oracle
                    Avion avion = new Avion(modelo, fabricante, capacidad, autonomia);
                    AvionDAO dao = new AvionDAO();
                    dao.registrarAvion(avion);

                    // Respuesta SOAP
                    String response =
                            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                                    "<soapenv:Body>" +
                                    "<return>Avión registrado correctamente en Oracle 19c</return>" +
                                    "</soapenv:Body>" +
                                    "</soapenv:Envelope>";

                    exchange.getResponseHeaders().add("Content-Type", "text/xml; charset=utf-8");
                    exchange.sendResponseHeaders(200, response.length());

                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes(StandardCharsets.UTF_8));
                    os.close();
                }
            });

            server.start();

            System.out.println("Servicio SOAP publicado SIN JAX-WS");
            System.out.println("Endpoint:");
            System.out.println("http://localhost:8080/AvionService");

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

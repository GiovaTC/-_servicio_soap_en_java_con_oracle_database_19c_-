package avion;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Publicador {

    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            server.createContext("/AvionService", (HttpExchange exchange) -> {

                // Si piden ?wsdl
                if ("GET".equals(exchange.getRequestMethod())
                        && exchange.getRequestURI().getQuery() != null
                        && exchange.getRequestURI().getQuery().equalsIgnoreCase("wsdl")) {

                    byte[] wsdl = Files.readAllBytes(
                            Paths.get("AvionService.wsdl")
                    );

                    exchange.getResponseHeaders().add("Content-Type", "text/xml");
                    exchange.sendResponseHeaders(200, wsdl.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(wsdl);
                    os.close();
                    return;
                }

                exchange.sendResponseHeaders(405, -1);
            });

            server.start();

            System.out.println("Servicio SOAP activo");
            System.out.println("WSDL disponible en:");
            System.out.println("http://localhost:8080/AvionService?wsdl");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

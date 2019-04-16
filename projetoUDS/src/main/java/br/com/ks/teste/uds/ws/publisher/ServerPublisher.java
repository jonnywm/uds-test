package br.com.ks.teste.uds.ws.publisher;

import br.com.ks.teste.uds.ws.publisher.resource.AppResources;
import java.io.IOException;
import java.net.URI;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

/**
 *
 * @author Jonny
 */
public class ServerPublisher {

    private static final String URL = "http://localhost:9090";
    private static HttpServer HTTP_SERVER = null;

    /**
     * Método responsável por publicar o endpoint http do serviço.
     */
    public static void start() {

        try {

            if (HTTP_SERVER == null || !HTTP_SERVER.isStarted()) {
                HTTP_SERVER = GrizzlyHttpServerFactory.createHttpServer(URI.create(URL), new AppResources());
            }

        } catch (NullPointerException | IllegalArgumentException ex) {
            Logger.getLogger(ServerPublisher.class.getSimpleName()).log(Level.SEVERE, (Supplier<String>) ex);

        }

    }

    /**
     * Método responsável para desativar o serviço http
     */
    public static void stop() {

        if (HTTP_SERVER != null && HTTP_SERVER.isStarted()) {
            HTTP_SERVER.stop();
        }

    }
}

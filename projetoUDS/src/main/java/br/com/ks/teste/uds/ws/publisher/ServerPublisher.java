package br.com.ks.teste.uds.ws.publisher;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import java.io.IOException;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.grizzly.http.server.HttpServer;

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
                ResourceConfig rc = new PackagesResourceConfig("br.com.ks.teste.uds");
                HTTP_SERVER = GrizzlyServerFactory.createHttpServer(URL, rc);
            }

        } catch (NullPointerException | IOException | IllegalArgumentException ex) {
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

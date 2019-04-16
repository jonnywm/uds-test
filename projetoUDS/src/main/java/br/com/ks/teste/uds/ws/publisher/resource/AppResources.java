package br.com.ks.teste.uds.ws.publisher.resource;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author Jonny
 */
public class AppResources extends ResourceConfig {

    public AppResources() {
        packages("br.com.ks.teste.uds");
        register(JacksonFeature.class);
    }

}

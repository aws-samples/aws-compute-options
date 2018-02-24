package org.aws.samples.compute.webapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;


@Path("/")
public class GreetingController {

    @Produces("text/plain")
    @GET
    public String greeting(@Context UriInfo requestUri) {
        String greetingEndpoint = getEndpoint("GREETING", requestUri.getRequestUri().getScheme());
        String nameEndpoint = getEndpoint("NAME", requestUri.getRequestUri().getScheme());

        Client client = ClientBuilder.newClient();
        String greeting = client
                .target(greetingEndpoint)
                .request(MediaType.TEXT_PLAIN)
                .get(String.class);

        String name = client
                .target(nameEndpoint)
                .request()
                .get(String.class);

        return greeting + " " + name;
    }

    private String getEndpoint(String type, String protocol) {
        String host = System.getenv(type + "_SERVICE_HOST");
        if (null == host) {
            throw new RuntimeException(type + "_SERVICE_HOST environment variable not found");
        }

        String port = System.getenv(type + "_SERVICE_PORT");
        if (null == port) {
            throw new RuntimeException(type + "_SERVICE_PORT environment variable not found");
        }

        String path = System.getenv(type + "_SERVICE_PATH");
        if (null == path) {
            throw new RuntimeException(type + "_SERVICE_PATH environment variable not found");
        }



        String endpoint = protocol + "://" + host + ":" + port + path;

        return endpoint;
    }

}

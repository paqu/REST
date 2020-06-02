package com.paqu.rest;

import com.paqu.rest.utils.CustomHeaders;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;


import java.io.IOException;
import java.net.URI;

public class Main {
    public static final int PORT = 1234;
    public static final String BASE_URI = "http://localhost:"+ PORT + "/";

    public static HttpServer startServer() {

        // JAX-RS resources and providers
        final ResourceConfig rc = new ResourceConfig().packages("com.paqu.rest");
        rc.register(DeclarativeLinkingFeature.class);
        rc.register(CustomHeaders.class);


        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println("Server listening on " + PORT + "\nHit enter to stop it...");
        System.in.read();
        server.stop();
    }
}
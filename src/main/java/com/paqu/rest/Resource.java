package com.paqu.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("helloworld")
public class Resource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getHelloWorld() {
        return "Hello World";
    }
}
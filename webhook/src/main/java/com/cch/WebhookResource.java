package com.cch;

import java.util.Map;


import org.jboss.logging.Logger;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


@Path("/webhook")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WebhookResource {
    @Inject
    Logger LOG;

    @POST
    @Path("/")
    public Response endepoint(Map<String, Object> content) {

        LOG.infof(" {} ", content);
        LOG.info("===================================================");
        content.forEach((k, v) -> System.out.println((k + ": " + v)));
        LOG.info("===================================================");

        return Response.status(Status.CREATED).build();

    }
}

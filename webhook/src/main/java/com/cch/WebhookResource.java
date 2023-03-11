package com.cch;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.logging.Logger;


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

package com.cch;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;

@Path("/pod")
public class PodResource {
    @Inject
    private KubernetesClient kubernetesClient;

    @GET
    @Path("/list/name")
    public List<String> podHostName(@QueryParam("namespace") String namespace) {
        return kubernetesClient.pods().inNamespace(namespace).list().getItems().stream().map(x -> x.getMetadata().getName()).toList();
    }

    @GET
    @Path("/list")
    public List<Pod> pods(@QueryParam("namespace") String namespace) {
        return kubernetesClient.pods().inNamespace(namespace).list().getItems();
    }
}

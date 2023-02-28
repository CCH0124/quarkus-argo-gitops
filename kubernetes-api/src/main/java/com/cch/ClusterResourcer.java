package com.cch;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/cluster")
public class ClusterResourcer {

    @Inject
    private KubernetesClient kubernetesClient;
    
    @GET
    @Path("/namespace")
    public List<Namespace> clusterNamespaces() {
        return kubernetesClient.namespaces().list().getItems();
    }

    @GET
    @Path("/nodes")
    public List<Node> clusterNodes() {
        return kubernetesClient.nodes().list().getItems();
    }

    @GET
    @Path("/node/status")
    public Response clusterNodeStatus(@QueryParam("nodeName") String nodeName) {
        var node = kubernetesClient.nodes().list().getItems().stream()
        .filter( x -> Objects.equals(x.getMetadata().getName(), nodeName))
        .findFirst();

        if(node.isPresent()) {
            return Response.ok(node.get().getStatus()).build();
        } 

        return Response.status(Status.NOT_FOUND).build();

    }
}

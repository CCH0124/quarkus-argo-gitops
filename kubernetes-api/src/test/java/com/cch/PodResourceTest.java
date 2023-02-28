package com.cch;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.PodListBuilder;
import io.fabric8.kubernetes.client.server.mock.KubernetesServer;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.kubernetes.client.KubernetesTestServer;
import io.quarkus.test.kubernetes.client.WithKubernetesTestServer;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;


@WithKubernetesTestServer
@QuarkusTest
public class PodResourceTest {
    @KubernetesTestServer
    KubernetesServer mockServer;

    @BeforeEach
    public void before() {
        final Pod pod1 = new PodBuilder().withNewMetadata().withName("pod1").withNamespace("test").and().build();
        final Pod pod2 = new PodBuilder().withNewMetadata().withName("pod2").withNamespace("test").and().build();

        // Set up Kubernetes so that our "pretend" pods are created
        mockServer.expect().get().withPath("/api/v1/namespaces/test/pods")
                .andReturn(200,
                        new PodListBuilder().withNewMetadata().withResourceVersion("1").endMetadata().withItems(pod1, pod2)
                                .build())
                .always();
    }

    @Test
    public void testInteractionWithAPIServer() {
        given().when()
        .queryParam("namespace", "test")
        .get("/pod/list")
        .then()
        .statusCode(200)
                .body("size()", is(2));
    }
}

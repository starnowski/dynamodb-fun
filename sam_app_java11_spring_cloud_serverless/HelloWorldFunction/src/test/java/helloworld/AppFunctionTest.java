package helloworld;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@FunctionalSpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class AppFunctionTest {

    @Autowired
    private WebTestClient client;
    @MockBean
    private AmazonDynamoDB dynamoDbAsyncClient;

    @Test
    void doesContainsCloud() {
        client.post().uri("/extractPayloadFromGatewayEvent").body(Mono.just("this is a cloud"), String.class).exchange()
                .expectStatus().isOk().expectBody(String.class).isEqualTo("true");
    }
}

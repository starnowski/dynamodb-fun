package helloworld;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import helloworld.dao.LeadsDao;
import helloworld.model.Leads;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
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
    @MockBean
    private LeadsDao leadsDao;

    @Test
    void leads() {
        // GIVEN
        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setBody("{\"name\": \"warehouse\", \"type\": \"CLOTHES RENTAL\", \"url\": \"http://warehouse.com/nosuch_address\"}");
        requestEvent.setHttpMethod("POST");
        requestEvent.setResource("/leads");
        Mockito.when(leadsDao.persist(Mockito.any(Leads.class))).then(new Answer<Leads>() {

            @Override
            public Leads answer(InvocationOnMock invocationOnMock) throws Throwable {
                return invocationOnMock.getArgument(0);
            }
        });

        // WHEN
        WebTestClient.ResponseSpec value = client.post().uri("/extractPayloadFromGatewayEvent").body(Mono.just(requestEvent), APIGatewayProxyRequestEvent.class).exchange();

        // THEN
        value.expectStatus().isOk().expectBody(String.class).isEqualTo("{\"statusCode\":200,\"headers\":{\"X-Custom-Header\":\"application/json\",\"Content-Type\":\"application/json\"},\"multiValueHeaders\":null,\"body\":\"{\\\"name\\\":\\\"warehouse\\\",\\\"type\\\":\\\"CLOTHES RENTAL\\\",\\\"url\\\":\\\"http://warehouse.com/nosuch_address\\\"}\",\"isBase64Encoded\":null}");
    }
}

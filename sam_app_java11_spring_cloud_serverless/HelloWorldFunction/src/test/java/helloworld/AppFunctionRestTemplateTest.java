package helloworld;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import helloworld.dao.LeadsDao;
import helloworld.dao.UserStatsDao;
import helloworld.model.Leads;
import helloworld.model.UserStat;
import helloworld.model.UserStatQueryRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JsonContentAssert;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.JsonPathAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@FunctionalSpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class AppFunctionRestTemplateTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @MockBean
    private AmazonDynamoDB dynamoDbAsyncClient;
    @MockBean
    private LeadsDao leadsDao;
    @MockBean
    private UserStatsDao userStatsDao;

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
        ResponseEntity<APIGatewayProxyResponseEvent> result = restTemplate.postForEntity("/extractPayloadFromGatewayEvent", requestEvent, APIGatewayProxyResponseEvent.class);

        // THEN
        Assertions.assertEquals(200, result.getStatusCode().value());
//        https://github.com/json-path/JsonPath
        DocumentContext documentContext = JsonPath.parse(result.getBody().getBody());
        Assertions.assertEquals("http://warehouse.com/nosuch_address", documentContext.read("url"));
        Assertions.assertEquals("warehouse", documentContext.read("name"));
        Assertions.assertEquals("CLOTHES RENTAL", documentContext.read("type"));
//        value.expectStatus().isOk().expectBody(String.class).isEqualTo("{\"statusCode\":200,\"headers\":{\"X-Custom-Header\":\"application/json\",\"Content-Type\":\"application/json\"},\"multiValueHeaders\":null,\"body\":\"{\\\"name\\\":\\\"warehouse\\\",\\\"type\\\":\\\"CLOTHES RENTAL\\\",\\\"url\\\":\\\"http://warehouse.com/nosuch_address\\\"}\",\"isBase64Encoded\":null}");
    }
}

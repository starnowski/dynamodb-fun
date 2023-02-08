package helloworld.handlers;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import helloworld.DynamoTestContainerTest;
import helloworld.model.UserStat;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserStatQueryRequestHandlerTest extends DynamoTestContainerTest {

    UserStatQueryRequestHandler tested;

    @BeforeEach
    public void setUp() {
        super.setUp();
        this.tested = this.appTestModule.providUserStatQueryRequestHandler();
    }

    @Test
    public void queryUserStats() throws JsonProcessingException, JSONException {
        // GIVEN
        LocalDateTime localDateTime = LocalDateTime.now();
        UserStat userStat = new UserStat();
        String userStatId = "queryUserStats";
        userStat.setUserId(userStatId);
        Long userStatTimestamp = localDateTime.plusMinutes(15).toEpochSecond(ZoneOffset.MIN);
        userStat.setTimestamp(userStatTimestamp);
        DynamoDBMapper mapper = new DynamoDBMapper(this.dynamoDbAsyncClient);
        mapper.save(userStat);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_id", userStatId);
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setBody(jsonObject.toString());

        // WHEN
        APIGatewayProxyResponseEvent lambdaResponse = tested.handlePostUserStatQueryRequestRequest(requestEvent, response);

        // THEN
        DocumentContext jsonContext = JsonPath.parse(lambdaResponse.getBody());
        assertEquals(userStatId, jsonContext.read("['results'][0]['user_id']"));
    }
}
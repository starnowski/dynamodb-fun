package helloworld.handlers;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import helloworld.DynamoTestContainerTest;
import helloworld.model.UserStat;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UserStatsPostHandlerTest extends DynamoTestContainerTest {

    @Autowired
    UserStatsPostHandler tested;

    @Test
    public void createUserStats() throws JsonProcessingException, JSONException {
        // GIVEN
        String name = "XCompany";
        Date date = Calendar.getInstance().getTime();
        Integer weight = 91;
        Integer bloodPressure = 123;
        DynamoDBMapper mapper = new DynamoDBMapper(DynamoTestContainerTest.dynamoDbAsyncClient);
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(name));
        eav.put(":val2", new AttributeValue().withN(Long.toString(date.getTime())));
        Map<String, String> ean = new HashMap<String, String>();
        ean.put("#t_attribute", "timestamp");
        DynamoDBQueryExpression<UserStat> queryExpression = new DynamoDBQueryExpression<UserStat>()
                .withKeyConditionExpression("user_id = :val1 and #t_attribute = :val2").withExpressionAttributeValues(eav)
                .withExpressionAttributeNames(ean);
        List<UserStat> latestUserStat = mapper.query(UserStat.class, queryExpression);
        Assertions.assertTrue(latestUserStat.isEmpty());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String text = sdf.format(date);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_id", name)
                .put("timestamp", text)
                .put("weight", weight)
                .put("blood_pressure", bloodPressure);
        //{"user_id": "1", "timestamp": "2021-11-15T00:35:26.501320", "weight": 83, "blood_pressure": 123}
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setBody(jsonObject.toString());

        // WHEN
        tested.handlePostUserStatRequest(requestEvent, response);

        // THEN
        latestUserStat = mapper.query(UserStat.class, queryExpression);
        assertFalse(latestUserStat.isEmpty());
        assertEquals(1, latestUserStat.size());
        assertEquals(name, latestUserStat.get(0).getUserId());
        assertEquals(date.getTime(), latestUserStat.get(0).getTimestamp());
        assertEquals(weight, latestUserStat.get(0).getWeight());
        assertEquals(bloodPressure, latestUserStat.get(0).getBloodPressure());
    }

    @Test
    public void createUserStatsProdJson() throws JsonProcessingException {
        // GIVEN
        String name = "ProdXXX";
        DynamoDBMapper mapper = new DynamoDBMapper(DynamoTestContainerTest.dynamoDbAsyncClient);
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(name));
        DynamoDBQueryExpression<UserStat> queryExpression = new DynamoDBQueryExpression<UserStat>()
                .withKeyConditionExpression("user_id = :val1").withExpressionAttributeValues(eav);
        List<UserStat> latestUserStat = mapper.query(UserStat.class, queryExpression);
        Assertions.assertTrue(latestUserStat.isEmpty());
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setBody("{\"user_id\": \"ProdXXX\", \"timestamp\": \"2021-11-16T01:03:12.055701\", \"weight\": 83, \"blood_pressure\": 123}");

        // WHEN
        response = tested.handlePostUserStatRequest(requestEvent, response);

        // THEN
        latestUserStat = mapper.query(UserStat.class, queryExpression);
        assertFalse(latestUserStat.isEmpty());
        assertEquals(1, latestUserStat.size());
        assertEquals(name, latestUserStat.get(0).getUserId());
        assertEquals(200, response.getStatusCode());
    }
}
package helloworld.handlers;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import helloworld.DynamoTestContainerTest;
import helloworld.model.UserStat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UserStatsPostHandlerTest extends DynamoTestContainerTest {

    UserStatsPostHandler tested;

    @BeforeEach
    public void setUp() {
        super.setUp();
        this.tested = this.appTestModule.provideUserStatsPostHandler();
    }

    @Test
    public void createUserStats() {
        // GIVEN
        String name = "XCompany";
        Long timestamp = Calendar.getInstance().getTimeInMillis();
        Integer weight = 91;
        Integer bloodPressure = 123;
        DynamoDBMapper mapper = new DynamoDBMapper(DynamoTestContainerTest.dynamoDbAsyncClient);
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(name));
        eav.put(":val2", new AttributeValue().withN(timestamp.toString()));
        Map<String, String> ean = new HashMap<String, String>();
        ean.put("#t_attribute", "timestamp");
        DynamoDBQueryExpression<UserStat> queryExpression = new DynamoDBQueryExpression<UserStat>()
                .withKeyConditionExpression("user_id = :val1 and #t_attribute = :val2").withExpressionAttributeValues(eav)
                .withExpressionAttributeNames(ean);
        List<UserStat> latestUserStat = mapper.query(UserStat.class, queryExpression);
        Assertions.assertTrue(latestUserStat.isEmpty());
        UserStat userStat = new UserStat();
        userStat.setUserId(name);
        userStat.setTimestamp(timestamp);
        userStat.setWeight(weight);
        userStat.setBloodPressure(bloodPressure);

        // WHEN
//        tested.persist(userStat);

        // THEN
        latestUserStat = mapper.query(UserStat.class, queryExpression);
        assertFalse(latestUserStat.isEmpty());
        assertEquals(1, latestUserStat.size());
        assertEquals(name, latestUserStat.get(0).getUserId());
        assertEquals(timestamp, latestUserStat.get(0).getTimestamp());
        assertEquals(weight, latestUserStat.get(0).getWeight());
        assertEquals(bloodPressure, latestUserStat.get(0).getBloodPressure());
    }
}
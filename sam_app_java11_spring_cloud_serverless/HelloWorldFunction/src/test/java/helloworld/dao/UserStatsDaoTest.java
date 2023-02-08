package helloworld.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import helloworld.DynamoTestContainerTest;
import helloworld.model.UserStat;
import helloworld.model.UserStatQueryRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UserStatsDaoTest extends DynamoTestContainerTest {

    @Autowired
    UserStatsDao tested;

    private static Stream<Arguments> provideShouldReturnCorrectNumberOfResultsBasedOnLimitParameter() {
        return Stream.of(
                Arguments.of(prepareUserStatsArray("x1", 1), UserStatQueryRequest.builder().userId("x1").limit(1L).build(), 1),
                Arguments.of(prepareUserStatsArray("x2", 3), UserStatQueryRequest.builder().userId("x2").build(), 3),
                Arguments.of(prepareUserStatsArray("x33", 3), UserStatQueryRequest.builder().userId("x33").limit(2L).build(), 2),
                Arguments.of(prepareUserStatsArray("x111", 5), UserStatQueryRequest.builder().userId("x111").limit(4L).build(), 4)
        );
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
        tested.persist(userStat);

        // THEN
        latestUserStat = mapper.query(UserStat.class, queryExpression);
        assertFalse(latestUserStat.isEmpty());
        assertEquals(1, latestUserStat.size());
        assertEquals(name, latestUserStat.get(0).getUserId());
        assertEquals(timestamp, latestUserStat.get(0).getTimestamp());
        assertEquals(weight, latestUserStat.get(0).getWeight());
        assertEquals(bloodPressure, latestUserStat.get(0).getBloodPressure());
    }

    @ParameterizedTest
    @MethodSource("provideShouldReturnCorrectNumberOfResultsBasedOnLimitParameter")
    public void shouldReturnCorrectNumberOfResultsBasedOnLimitParameter(UserStat[] userStats, UserStatQueryRequest queryRequest, int expectedUserStatsNumberOfFetchedResults) throws InterruptedException {
        // GIVEN
        DynamoDBMapper mapper = new DynamoDBMapper(this.dynamoDbAsyncClient);
        mapper.batchSave(userStats);

        // WHEN
        QueryResultPage<UserStat> results = tested.query(queryRequest);

        // THEN
        assertEquals(expectedUserStatsNumberOfFetchedResults, results.getCount());
    }

    private static UserStat[] prepareUserStatsArray(String userStatId, int numberOfResults) {
        LocalDateTime localDateTime = LocalDateTime.now();
        List<UserStat> results = new ArrayList<>();
        for (int i = 0; i < numberOfResults; i++) {
            UserStat userStat = new UserStat();
            userStat.setUserId(userStatId);
            Long userStatTimestamp = localDateTime.plusMinutes(i).toEpochSecond(ZoneOffset.MIN);
            userStat.setTimestamp(userStatTimestamp);
            results.add(userStat);
        }
        return results.toArray(new UserStat[0]);
    }

    @Test
    public void shouldReturnResultsThatWereCreatedAfterSpecificTimestamp() {
        // GIVEN
        LocalDateTime localDateTime = LocalDateTime.now();
        UserStat userStat = new UserStat();
        String userStatId = "ReturnResultThatWereCreatedAfterSpecificTimestamp";
        userStat.setUserId(userStatId);
        Long userStatTimestamp = localDateTime.plusMinutes(15).toEpochSecond(ZoneOffset.MIN);
        userStat.setTimestamp(userStatTimestamp);
        DynamoDBMapper mapper = new DynamoDBMapper(this.dynamoDbAsyncClient);
        mapper.save(userStat);
        UserStatQueryRequest queryRequest = UserStatQueryRequest.builder().userId(userStatId).after_timestamp(localDateTime.toEpochSecond(ZoneOffset.MIN)).build();

        // WHEN
        QueryResultPage<UserStat> results = tested.query(queryRequest);

        // THEN
        assertEquals(1, results.getCount());
        assertEquals(userStatId, results.getResults().get(0).getUserId());
    }

    @Test
    public void shouldReturnNoResultsWhenThereAreNoCreatedRecordsAfterSpecificTimestamp() {
        // GIVEN
        LocalDateTime localDateTime = LocalDateTime.now();
        UserStat userStat = new UserStat();
        String userStatId = "test4";
        userStat.setUserId(userStatId);
        Long userStatTimestamp = localDateTime.minusMinutes(30L).toEpochSecond(ZoneOffset.MIN);
        userStat.setTimestamp(userStatTimestamp);
        DynamoDBMapper mapper = new DynamoDBMapper(this.dynamoDbAsyncClient);
        mapper.save(userStat);
        UserStatQueryRequest queryRequest = UserStatQueryRequest.builder().userId(userStatId).after_timestamp(localDateTime.toEpochSecond(ZoneOffset.MIN)).build();

        // WHEN
        QueryResultPage<UserStat> results = tested.query(queryRequest);

        // THEN
        assertEquals(0, results.getCount());
    }
}
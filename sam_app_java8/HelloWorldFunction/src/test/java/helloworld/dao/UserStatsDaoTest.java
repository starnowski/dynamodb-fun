package helloworld.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import helloworld.model.UserStat;
import helloworld.model.UserStatQueryRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Stream;

class UserStatsDaoTest extends DynamoTestContainerTest {

    UserStatsDao tested;

    private static Stream<Arguments> provideShouldReturnCorrectNumberOfResultsBasedOnLimitParameter() {
        return Stream.of(
                Arguments.of(prepareUserStatsArray("x1", 1), UserStatQueryRequest.builder().userId("x1").limit(1L), 1),
                Arguments.of(prepareUserStatsArray("x2", 3), UserStatQueryRequest.builder().userId("x1"), 3),
                Arguments.of(prepareUserStatsArray("x33", 3), UserStatQueryRequest.builder().userId("x1").limit(2L), 2),
                Arguments.of(prepareUserStatsArray("x111", 5), UserStatQueryRequest.builder().userId("x1").limit(4L), 4)
        );
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
        this.tested = this.appTestModule.provideUserStatsDao();
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
        Assertions.assertFalse(latestUserStat.isEmpty());
        Assertions.assertEquals(1, latestUserStat.size());
        Assertions.assertEquals(name, latestUserStat.get(0).getUserId());
        Assertions.assertEquals(timestamp, latestUserStat.get(0).getTimestamp());
        Assertions.assertEquals(weight, latestUserStat.get(0).getWeight());
        Assertions.assertEquals(bloodPressure, latestUserStat.get(0).getBloodPressure());
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

    @ParameterizedTest
    @MethodSource("provideShouldReturnCorrectNumberOfResultsBasedOnLimitParameter")
    public void shouldReturnCorrectNumberOfResultsBasedOnLimitParameter(UserStat[] userStats, UserStatQueryRequest queryRequest, int expectedUserStatsNumberOfFetchedResults) {
        // GIVEN
        for (UserStat userStat : userStats) {
            DynamoDBMapper mapper = new DynamoDBMapper(this.dynamoDbAsyncClient);
            mapper.save(userStat);
        }

        // WHEN
        PaginatedQueryList<UserStat> results = tested.query(queryRequest);

        // THEN
        Assertions.assertEquals(expectedUserStatsNumberOfFetchedResults, results.stream().count());
        Assertions.assertEquals(userStats.length, results.size());
    }
}
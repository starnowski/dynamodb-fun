package helloworld.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import helloworld.model.UserStat;
import helloworld.model.UserStatQueryRequest;

import java.util.HashMap;
import java.util.Map;

public class UserStatsDao {

    AmazonDynamoDB amazonDynamoDB;

    public UserStatsDao(AmazonDynamoDB amazonDynamoDB) {
        this.amazonDynamoDB = amazonDynamoDB;
    }

    public UserStat persist(UserStat userStat) {
        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
        mapper.save(userStat);
        return userStat;
    }

    public QueryResultPage<UserStat> query(UserStatQueryRequest queryRequest) {
        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(queryRequest.getUserId()));
        Map<String, String> ean = new HashMap<String, String>();
        DynamoDBQueryExpression<UserStat> queryExpression = new DynamoDBQueryExpression<UserStat>()
                .withKeyConditionExpression("user_id = :val1")
                .withExpressionAttributeValues(eav);
        if (queryRequest.getLimit() != null) {
            queryExpression.withLimit(Math.toIntExact(queryRequest.getLimit()));
        }
        if (!ean.isEmpty()) {
            queryExpression.withExpressionAttributeNames(ean);
        }
        queryExpression.withScanIndexForward(false);
//        return mapper.query(UserStat.class, queryExpression);
        return mapper.queryPage(UserStat.class, queryExpression);
    }
}

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
        StringBuilder keyConditionExpressionBuilder = new StringBuilder();
        keyConditionExpressionBuilder.append("user_id = :val1");
        Map<String, String> ean = new HashMap<String, String>();
        DynamoDBQueryExpression<UserStat> queryExpression = new DynamoDBQueryExpression<UserStat>();
        if (queryRequest.getLimit() != null) {
            queryExpression.withLimit(Math.toIntExact(queryRequest.getLimit()));
        }
        if (queryRequest.getAfter_timestamp() != null) {
            keyConditionExpressionBuilder.append(" and #t_attribute >= :val2");
            eav.put(":val2", new AttributeValue().withN(queryRequest.getAfter_timestamp().toString()));
            ean.put("#t_attribute", "timestamp");
        }
        if (!eav.isEmpty()) {
            queryExpression.withExpressionAttributeValues(eav);
        }
        if (!ean.isEmpty()) {
            queryExpression.withExpressionAttributeNames(ean);
        }
        queryExpression.withKeyConditionExpression(keyConditionExpressionBuilder.toString());
        queryExpression.withScanIndexForward(false);
        return mapper.queryPage(UserStat.class, queryExpression);
    }
}

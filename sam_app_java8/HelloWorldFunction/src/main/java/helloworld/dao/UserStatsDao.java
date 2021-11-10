package helloworld.dao;

import com.amazonaws.AmazonWebServiceClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import helloworld.model.UserStat;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import javax.inject.Inject;

public class UserStatsDao {

    @Inject
    AmazonDynamoDB amazonDynamoDB;

    public UserStat persist(UserStat userStat)
    {
        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
        mapper.save(userStat);
        return userStat;
    }
}

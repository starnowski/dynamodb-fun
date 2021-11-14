package helloworld.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import helloworld.model.UserStat;
import helloworld.model.UserStatQueryRequest;

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

    public PaginatedQueryList<UserStat> query(UserStatQueryRequest queryRequest) {
        //TODO
        return null;
    }
}

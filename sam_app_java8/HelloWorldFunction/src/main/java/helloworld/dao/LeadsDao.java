package helloworld.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import helloworld.model.Leads;

public class LeadsDao {

    private AmazonDynamoDB amazonDynamoDB;

    public LeadsDao(AmazonDynamoDB amazonDynamoDB) {
        this.amazonDynamoDB = amazonDynamoDB;
    }

    public Leads persist(Leads lead)
    {
        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
        mapper.save(lead);
        return lead;
    }
}

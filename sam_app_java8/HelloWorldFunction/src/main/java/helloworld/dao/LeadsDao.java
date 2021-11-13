package helloworld.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import helloworld.model.Leads;

import javax.inject.Inject;

public class LeadsDao {

    @Inject
    AmazonDynamoDB amazonDynamoDB;

    public Leads persist(Leads lead)
    {
        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
        mapper.save(lead);
        return lead;
    }
}

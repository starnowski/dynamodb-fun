package helloworld.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import helloworld.DynamoTestContainerTest;
import helloworld.model.Leads;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LeadsDaoTest extends DynamoTestContainerTest {

    @Autowired
    LeadsDao tested;

    @Test
    public void createLeads() {
        // GIVEN
        String name = "XCompany";
        String type = "Software";
        DynamoDBMapper mapper = new DynamoDBMapper(DynamoTestContainerTest.dynamoDbAsyncClient);
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(name));
        eav.put(":val2", new AttributeValue().withS(type));
        Map<String, String> ean = new HashMap<String, String>();
        ean.put("#n_attribute", "name");
        ean.put("#t_attribute", "type");
        DynamoDBQueryExpression<Leads> queryExpression = new DynamoDBQueryExpression<Leads>()
                .withKeyConditionExpression("#n_attribute = :val1 and #t_attribute = :val2").withExpressionAttributeValues(eav)
                .withExpressionAttributeNames(ean);
        List<Leads> latestLeads = mapper.query(Leads.class, queryExpression);
        Assertions.assertTrue(latestLeads.isEmpty());
        Leads leads = new Leads();
        leads.setName(name);
        leads.setType(type);
        leads.setUrl("XXXXXX");

        // WHEN
        tested.persist(leads);

        // THEN
        latestLeads = mapper.query(Leads.class, queryExpression);
        Assertions.assertFalse(latestLeads.isEmpty());
        Assertions.assertEquals(1, latestLeads.size());
        Assertions.assertEquals(name, latestLeads.get(0).getName());
        Assertions.assertEquals(type, latestLeads.get(0).getType());
        Assertions.assertEquals("XXXXXX", latestLeads.get(0).getUrl());
    }
}
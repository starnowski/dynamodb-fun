package helloworld.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import helloworld.dao.LeadsDao;
import helloworld.dao.UserStatsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoModules {

    @Bean
    public LeadsDao leadsDao(@Autowired AmazonDynamoDB dynamoDb) {
        return new LeadsDao(dynamoDb);
    }

    @Bean
    public UserStatsDao userStatsDao(@Autowired AmazonDynamoDB dynamoDb) {
        return new UserStatsDao(dynamoDb);
    }
}

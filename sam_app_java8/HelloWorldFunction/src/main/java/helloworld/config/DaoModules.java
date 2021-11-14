package helloworld.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import dagger.Module;
import dagger.Provides;
import helloworld.dao.LeadsDao;
import helloworld.dao.UserStatsDao;

import javax.inject.Singleton;

@Module
public class DaoModules {

    @Singleton
    @Provides
    public LeadsDao leadsDao(AmazonDynamoDB dynamoDb) {
        return new LeadsDao(dynamoDb);
    }

    @Singleton
    @Provides
    public UserStatsDao userStatsDao(AmazonDynamoDB dynamoDb) {
        return new UserStatsDao(dynamoDb);
    }
}

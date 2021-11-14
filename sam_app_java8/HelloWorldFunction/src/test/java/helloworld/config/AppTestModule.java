package helloworld.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import dagger.BindsInstance;
import dagger.Component;
import helloworld.dao.LeadsDao;

import javax.inject.Singleton;

@Singleton
@Component(modules = {DaoModules.class, MapperModule.class})
public interface AppTestModule extends AppModule{

    LeadsDao provideLeadsDao();
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder amazonDynamoDB(AmazonDynamoDB amazonDynamoDB);
        AppTestModule build();
    }
}
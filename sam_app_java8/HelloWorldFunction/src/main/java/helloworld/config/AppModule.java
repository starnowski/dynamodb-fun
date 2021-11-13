package helloworld.config;

import dagger.Module;
import dagger.Provides;
import helloworld.dao.LeadsDao;

import javax.inject.Singleton;

@Module
public class AppModule {

    @Singleton
    @Provides
    LeadsDao leadsDao() {
        return new LeadsDao();
    }
}

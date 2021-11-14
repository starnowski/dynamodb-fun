package helloworld.config;

import dagger.Component;
import helloworld.App;

import javax.inject.Singleton;

@Singleton
@Component(modules = {AppDynamoDBModule.class, DaoModules.class, MapperModule.class})
public interface AppModule {

    void inject(App app);
}

package helloworld.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class MapperModule {
    @Singleton
    @Provides
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}

package helloworld.config;

import helloworld.dao.LeadsDao;
import helloworld.dao.UserStatsDao;
import helloworld.handlers.UserStatQueryRequestHandler;
import helloworld.handlers.UserStatsPostHandler;
import org.springframework.context.annotation.Configuration;

@Configuration
public interface AppTestModule {

    LeadsDao provideLeadsDao();

    UserStatsDao provideUserStatsDao();

    UserStatsPostHandler provideUserStatsPostHandler();

    UserStatQueryRequestHandler providUserStatQueryRequestHandler();
}
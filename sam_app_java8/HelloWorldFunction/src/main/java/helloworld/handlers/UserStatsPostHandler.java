package helloworld.handlers;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helloworld.dao.UserStatsDao;
import helloworld.model.UserStat;

import javax.inject.Inject;

public class UserStatsPostHandler {

    @Inject
    ObjectMapper objectMapper;
    @Inject
    UserStatsDao userStatsDao;

    @Inject
    public UserStatsPostHandler() {
    }

    public APIGatewayProxyResponseEvent handlePostUserStatRequest(final APIGatewayProxyRequestEvent input, final APIGatewayProxyResponseEvent response) throws JsonProcessingException {
        UserStat userStat = objectMapper.readValue(input.getBody(), UserStat.class);
        userStat = userStatsDao.persist(userStat);
        String output = objectMapper.writeValueAsString(userStat);
        return response
                .withStatusCode(200)
                .withBody(output);
    }
}

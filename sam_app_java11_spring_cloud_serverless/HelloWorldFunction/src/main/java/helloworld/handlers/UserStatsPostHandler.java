package helloworld.handlers;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helloworld.dao.UserStatsDao;
import helloworld.model.UserStat;
import helloworld.model.UserStatDto;
import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserStatsPostHandler {

    final BoundMapperFacade<UserStatDto, UserStat> userStatDtoMapper;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserStatsDao userStatsDao;

    public UserStatsPostHandler() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder()
                .build();
        this.userStatDtoMapper = mapperFactory.getMapperFacade(UserStatDto.class, UserStat.class);
    }

    public APIGatewayProxyResponseEvent handlePostUserStatRequest(final APIGatewayProxyRequestEvent input, final APIGatewayProxyResponseEvent response) throws JsonProcessingException {
        UserStatDto dto = objectMapper.readValue(input.getBody(), UserStatDto.class);
        UserStat userStat = userStatsDao.persist(mapToValue(dto));
        String output = objectMapper.writeValueAsString(mapToDto(userStat));
//        return output;
        return response
                .withStatusCode(200)
                .withBody(output);
    }

    private UserStat mapToValue(UserStatDto dto) {
        UserStat value = userStatDtoMapper.map(dto);
        value.setTimestamp(dto.getTimestamp() == null ? null : dto.getTimestamp().getTime());
        return value;
    }

    private UserStatDto mapToDto(UserStat userStat) {
        UserStatDto dto = userStatDtoMapper.mapReverse(userStat);
        dto.setTimestamp(userStat == null ? null : new Date(userStat.getTimestamp()));
        return dto;
    }
}

package helloworld.handlers;

import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helloworld.dao.UserStatsDao;
import helloworld.model.*;
import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static java.util.stream.Collectors.toList;


@Component
public class UserStatQueryRequestHandler {
    final BoundMapperFacade<UserStatQueryDto, UserStatQueryRequest> userStatQueryDtoMapper;
    final BoundMapperFacade<UserStatDto, UserStat> userStatDtoMapper;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserStatsDao userStatsDao;

    @Autowired
    public UserStatQueryRequestHandler() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder()
                .build();
        this.userStatQueryDtoMapper = mapperFactory.getMapperFacade(UserStatQueryDto.class, UserStatQueryRequest.class);
        this.userStatDtoMapper = mapperFactory.getMapperFacade(UserStatDto.class, UserStat.class);
    }

    public APIGatewayProxyResponseEvent handlePostUserStatQueryRequestRequest(final APIGatewayProxyRequestEvent input, final APIGatewayProxyResponseEvent response) throws JsonProcessingException {
        UserStatQueryDto dto = objectMapper.readValue(input.getBody(), UserStatQueryDto.class);
        QueryResultPage<UserStat> results = userStatsDao.query(mapToValue(dto));
        UserStatSearchResponse userStatSearchResponse = new UserStatSearchResponse();
        userStatSearchResponse.setResults(Optional.ofNullable(results.getResults()).orElse(new ArrayList<>()).stream().map(this::mapToDto).collect(toList()));
        String output = objectMapper.writeValueAsString(userStatSearchResponse);
        return response
                .withStatusCode(200)
                .withBody(output);
    }

    private UserStatQueryRequest mapToValue(UserStatQueryDto dto) {
        UserStatQueryRequest value = userStatQueryDtoMapper.map(dto);
        value.setAfter_timestamp(dto.getAfter_timestamp() == null ? null : dto.getAfter_timestamp().getTime());
        return value;
    }

    private UserStatDto mapToDto(UserStat userStat) {
        UserStatDto dto = userStatDtoMapper.mapReverse(userStat);
        dto.setTimestamp(userStat.getTimestamp() == null ? null : new Date(userStat.getTimestamp()));
        return dto;
    }
}

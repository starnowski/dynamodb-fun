package helloworld;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helloworld.dao.LeadsDao;
import helloworld.dao.UserStatsDao;
import helloworld.handlers.UserStatQueryRequestHandler;
import helloworld.handlers.UserStatsPostHandler;
import helloworld.model.Leads;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Handler for requests to Lambda function.
 */
@SpringBootApplication
public class App
//        implements ApplicationContextInitializer<GenericApplicationContext>
{

    private static final Log logger = LogFactory.getLog(App.class);

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    LeadsDao leadsDao;
    @Autowired
    UserStatsDao userStatsDao;
    @Autowired
    UserStatsPostHandler userStatsPostHandler;
    @Autowired
    UserStatQueryRequestHandler userStatQueryRequestHandler;

    String initializationError;

    public static void main(String[] args) {
        logger.info("==> Starting: LambdaApplication");
        if (!ObjectUtils.isEmpty(args)) {
            logger.info("==>  args: " + Arrays.asList(args));
        }
        SpringApplication.run(App.class, args);
//        FunctionalSpringApplication.run(App.class, args);
    }

    @Bean
    public Function<String, APIGatewayProxyResponseEvent>extractPayloadFromString() {
        return input -> {
                JSONObject jsonObject = new JSONObject(input);
                APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent();
                event.setBody(jsonObject.getString("body"));
                event.setResource(jsonObject.getString("resource"));
                event.setHttpMethod(jsonObject.getString("httpMethod"));
                return extractPayloadFromGatewayEvent().apply(event);
        };
    }

    @Bean
    public Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> extractPayloadFromGatewayEvent() {
        return input -> {
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("X-Custom-Header", "application/json");

            APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                    .withHeaders(headers);

            try {
                if (initializationError != null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("message", initializationError);
//                    return jsonObject.toString();
                    return response
                            .withStatusCode(200)
                            .withBody(jsonObject.toString());
                }
                if (input != null) {
                    if ("/leads".equals(input.getResource()) && "POST".equals(input.getHttpMethod())) {
                        return handlePostLeadsRequest(input, response);
                    }
                    if ("/user_stats".equals(input.getResource()) && "POST".equals(input.getHttpMethod())) {
                        return userStatsPostHandler.handlePostUserStatRequest(input, response);
                    }
                    if ("/user_stats/search".equals(input.getResource()) && "POST".equals(input.getHttpMethod())) {
                        return userStatQueryRequestHandler.handlePostUserStatQueryRequestRequest(input, response);
                    }
                }
                final String pageContents = this.getPageContents("https://checkip.amazonaws.com");
                String output = String.format("{ \"message\": \"hello world\", \"location\": \"%s\" }", pageContents);

//                return output;
                return response
                        .withStatusCode(200)
                        .withBody(output);
            } catch (Exception e) {
                String output = String.format("{ \"exceptionMessage\": \"%s\" }", e.getMessage());
//                return output;
                return response
                        .withBody(output)
                        .withStatusCode(500);
            }
        };
    }

    private String getPageContents(String address) throws IOException {
        URL url = new URL(address);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

    private APIGatewayProxyResponseEvent handlePostLeadsRequest(final APIGatewayProxyRequestEvent input, APIGatewayProxyResponseEvent response) throws JsonProcessingException {
        Leads leads = objectMapper.readValue(input.getBody(), Leads.class);
        leads = leadsDao.persist(leads);
        String output = objectMapper.writeValueAsString(leads);
//        return output;
        return response
                .withStatusCode(200)
                .withBody(output);
    }

    //https://github.com/maciejwalkowiak/aws-sam-spring-cloud-function-template
//    @Override
//    public void initialize(GenericApplicationContext context) {
//        context.registerBean("extractPayloadFromGatewayEvent", FunctionRegistration.class,
//                () -> new FunctionRegistration<>(extractPayloadFromGatewayEvent())
//                        .type(FunctionType.from(APIGatewayProxyRequestEvent.class).to(String.class)));
//    }
}

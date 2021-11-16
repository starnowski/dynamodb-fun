package helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helloworld.config.AppModule;
import helloworld.config.DaggerAppModule;
import helloworld.dao.LeadsDao;
import helloworld.dao.UserStatsDao;
import helloworld.handlers.UserStatQueryRequestHandler;
import helloworld.handlers.UserStatsPostHandler;
import helloworld.model.Leads;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Inject
    ObjectMapper objectMapper;
    @Inject
    LeadsDao leadsDao;
    @Inject
    UserStatsDao userStatsDao;
    @Inject
    UserStatsPostHandler userStatsPostHandler;
    @Inject
    UserStatQueryRequestHandler userStatQueryRequestHandler;

    String initializationError;

    public App() {
        try {
            AppModule module = DaggerAppModule.create();
            module.inject(this);
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            initializationError = sw.toString();
        }
    }

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        try {
            if (initializationError != null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("message", initializationError);
                String payload = jsonObject.toString();
                return response
                        .withStatusCode(200)
                        .withBody(payload);
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

            return response
                    .withStatusCode(200)
                    .withBody(output);
        } catch (Exception e) {
            String output = String.format("{ \"exceptionMessage\": \"%s\" }", e.getMessage());
            return response
                    .withBody(output)
                    .withStatusCode(500);
        }
    }

    private String getPageContents(String address) throws IOException {
        URL url = new URL(address);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

    private APIGatewayProxyResponseEvent handlePostLeadsRequest(final APIGatewayProxyRequestEvent input, final APIGatewayProxyResponseEvent response) throws JsonProcessingException {
        Leads leads = objectMapper.readValue(input.getBody(), Leads.class);
        leads = leadsDao.persist(leads);
        String output = objectMapper.writeValueAsString(leads);
        return response
                .withStatusCode(200)
                .withBody(output);
    }
}

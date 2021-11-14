package helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import helloworld.config.AppModule;
import helloworld.config.DaggerAppModule;
import helloworld.dao.LeadsDao;
import helloworld.dao.UserStatsDao;
import helloworld.model.Leads;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public App() {
        AppModule module = DaggerAppModule.create();
        module.inject(this);
    }

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        try {
            if ("leads".equals(input.getResource()) && "POST".equals(input.getHttpMethod())) {
                return handlePostLeadsRequest(input, response);
            }
            final String pageContents = this.getPageContents("https://checkip.amazonaws.com");
            String output = String.format("{ \"message\": \"hello world\", \"location\": \"%s\" }", pageContents);

            return response
                    .withStatusCode(200)
                    .withBody(output);
        } catch (IOException e) {
            return response
                    .withBody("{}")
                    .withStatusCode(500);
        }
    }

    private String getPageContents(String address) throws IOException {
        URL url = new URL(address);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

    private APIGatewayProxyResponseEvent handlePostLeadsRequest(final APIGatewayProxyRequestEvent input, final APIGatewayProxyResponseEvent response) {
        Leads leads = objectMapper.convertValue(input.getBody(), Leads.class);
        leadsDao.persist(leads);
        String output = String.format("{ \"name\": \"%s\", \"type\": \"%s\", \"url\": \"%s\" }", leads.getName(), leads.getType(), leads.getUrl());
        return response
                .withStatusCode(200)
                .withBody(output);
    }
}

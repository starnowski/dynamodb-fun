package helloworld;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;

import static org.junit.Assert.*;

public class AppTest {
    @Test
    public void successfulResponse() {
        App app = new App();
        Message<APIGatewayProxyResponseEvent> message = app.extractPayloadFromGatewayEvent().apply(null);
        APIGatewayProxyResponseEvent result = message.getPayload();
//        assertEquals(result.getStatusCode().intValue(), 200);
//        assertEquals(result.getHeaders().get("Content-Type"), "application/json");
        String content = result.getBody();
        assertNotNull(content);
        assertTrue(content.contains("\"message\""));
        assertTrue(content.contains("\"hello world\""));
        assertTrue(content.contains("\"location\""));
    }
}

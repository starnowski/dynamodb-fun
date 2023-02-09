package helloworld;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class AppTest {
    @Test
    public void successfulResponse() {
        App app = new App();
        String result = app.extractPayloadFromGatewayEvent().apply(null);
//        assertEquals(result.getStatusCode().intValue(), 200);
//        assertEquals(result.getHeaders().get("Content-Type"), "application/json");
        String content = result;
        assertNotNull(content);
        assertTrue(content.contains("\"message\""));
        assertTrue(content.contains("\"hello world\""));
        assertTrue(content.contains("\"location\""));
    }
}

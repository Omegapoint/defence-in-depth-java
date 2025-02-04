package defence.in.depth.system;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

public class BaseTests {

    private final Properties systemTestProperties;

    final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

    public BaseTests() {

        this.systemTestProperties = new Properties();
        InputStream iStream;
        try {
            iStream = new ClassPathResource("application-system.properties").getInputStream();
            systemTestProperties.load(iStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getBaseUrl() {
        return systemTestProperties.getProperty("baseUrl");
    }

    public String getTokenUrl() {
        return systemTestProperties.getProperty("tokenUrl");
    }

    public String getClientId() {
        return systemTestProperties.getProperty("validClient.clientId");
    }

    public String getClientSecret() {
        return systemTestProperties.getProperty("validClient.clientSecret");
    }

    public String getJwtByScope(String scope) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String body =
                String.format("client_id=%s&client_secret=%s&scope=%s&grant_type=client_credentials",
                        getClientId(),
                        getClientSecret(),
                        scope);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(getTokenUrl()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode jsonNode = new ObjectMapper().readValue(response.body(), JsonNode.class);
        return jsonNode.get("access_token").textValue();
    }

}

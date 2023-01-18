package defence.in.depth.system;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@Tag("System")
public class ErrorTests extends BaseTests {

    @Test
    public void throwWithValidToken_ShouldReturn500AndNoDetails() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.noBody())
                .header("Authorization", "Bearer " + getJwtByScope("products.read"))
                .uri(URI.create(getBaseUrl() + "/api/error"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

        // Verify DTO to only contain expected values, by using a strict objectMapper
        ErrorDTO errorResponse = objectMapper.readValue(response.body(), ErrorDTO.class);
        ErrorDTO expectedValues = new ErrorDTO("500", "Internal Server Error", "/api/error");
        assertThat(errorResponse)
                .usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(expectedValues);
        assertThat(errorResponse)
                .extracting("timestamp")
                .satisfies(field -> assertThat(Instant.parse((String) field)).isBefore(Instant.now()));
    }


    private record ErrorDTO(String status, String error, String path, String timestamp, String derp) {
        private ErrorDTO(String status, String error, String path) {
            this(status, error, path, null, null);
        }
    }

}
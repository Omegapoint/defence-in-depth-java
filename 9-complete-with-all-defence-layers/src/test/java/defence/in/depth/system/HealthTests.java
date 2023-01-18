package defence.in.depth.system;

import ch.qos.logback.core.net.ssl.KeyStoreFactoryBean;
import org.apache.http.ssl.SSLContextBuilder;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


// The Health controller tests verify that we run the correct version, has mandatory JWT access control,
// handle exceptions and return recommended security headers.
@Tag("System")
public class HealthTests extends BaseTests {

    @Test
    public void livenessAnonymous_ShouldReturn200AndCorrectVersion() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(getBaseUrl() + "/api/health/live")).build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body()).contains("\"version\":\"1.0.0\"");
    }

    @Test
    public void readinessWithValidToken_ShouldReturn200() throws IOException, InterruptedException {

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(getBaseUrl() + "/api/health/ready")).header("Authorization", "Bearer " + getJwtByScope("products.read")).build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body()).contains("\"version\":\"1.0.0\"");
    }

    @Test
    public void readinessWithNoToken_ShouldReturn401() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(getBaseUrl() + "/api/health/ready")).build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void readinessWithInvalidToken_ShouldReturn401() throws IOException, InterruptedException {
        //Use a token from jwt.io, which has an invalid issuer for our API
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(getBaseUrl() + "/api/health/ready")).header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c").build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void readinessHttp_ShouldReturn405() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost/api/health/ready")).build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Note that this will fail when running without NGINX
        assertThat(response.statusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED.value());
    }

    @Test
    public void livenessAnonymous_ReturnsSecurityHeaders() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();


        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(getBaseUrl() + "/api/health/live")).build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Verify according to https://cheatsheetseries.owasp.org/cheatsheets/REST_Security_Cheat_Sheet.html#security-headers
        // Note that this will fail when running without NGINX
        SoftAssertions.assertSoftly(assertions -> {
            assertThat(response.headers()).isNotNull();
            assertThat(response.headers().map()).isNotNull();
            assertThat(response.headers().map()).anySatisfy((key, value) -> {
                assertThat(key).isEqualTo("cache-control");
                assertThat(toString(value)).contains("no-store");
            });
            assertThat(response.headers().map()).anySatisfy((key, value) -> {
                assertThat(key).isEqualTo("content-security-policy");
                assertThat(toString(value)).contains("frame-ancestors 'none'");
            });
            assertThat(response.headers().map()).anySatisfy((key, value) -> {
                assertThat(key).isEqualTo("content-type");
                assertThat(toString(value)).contains("application/json");
            });
            assertThat(response.headers().map()).anySatisfy((key, value) -> {
                assertThat(key).isEqualTo("strict-transport-security");
                assertThat(toString(value)).contains("max-age=31536000 ; includeSubDomains");
            });
            assertThat(response.headers().map()).anySatisfy((key, value) -> {
                assertThat(key).isEqualTo("x-content-type-options");
                assertThat(toString(value)).contains("nosniff");
            });
            assertThat(response.headers().map()).anySatisfy((key, value) -> {
                assertThat(key).isEqualTo("x-frame-options");
                assertThat(toString(value)).contains("DENY");
            });

        });
    }

    @Test
    public void livenessAnonymous_TLS_1_3_Only() throws NoSuchAlgorithmException, IOException, InterruptedException, KeyManagementException, KeyStoreException {
        // Since we only allow TLS 1.3 we only need to verify that we only accept TLS 1.3 and
        // there are no validation errors (according to .NET default policy) to assert sufficient TLS quality.
        // For TLS 1.2 we should also verify strong ciphers according to e g
        // https://openid.bitbucket.io/fapi/fapi-2_0-security-profile.html#section-5.2.2
        SSLContext contextTls13 = SSLContextBuilder.create().setProtocol("TLSv1.3").build();
        HttpClient httpClient13 = HttpClient.newBuilder()
                .sslContext(contextTls13)
                .build();

        SSLContext contextTls12 = SSLContextBuilder.create().setProtocol("TLSv1.3").build();
        HttpClient httpClient12 = HttpClient.newBuilder()
                .sslContext(contextTls12)
                .build();

        SSLContext contextTls11 = SSLContextBuilder.create().setProtocol("TLSv1.3").build();
        HttpClient httpClient11 = HttpClient.newBuilder()
                .sslContext(contextTls11)
                .build();


        SSLContext contextSsl3 = SSLContextBuilder.create().setProtocol("TLSv1.3").build();
        HttpClient httpClientSsl3 = HttpClient.newBuilder()
                .sslContext(contextSsl3)
                .build();

        SSLContext contextSsl2 = SSLContextBuilder.create().setProtocol("TLSv1.3").build();
        HttpClient httpClientSsl2 = HttpClient.newBuilder()
                .sslContext(contextSsl2)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(getBaseUrl() + "/api/health/live"))
                .build();

        HttpResponse<String> response = httpClient13.send(request, HttpResponse.BodyHandlers.ofString());

        // Note that this will fail when running without NGINX
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThrows(Exception.class, () -> httpClient12.send(request, HttpResponse.BodyHandlers.ofString()));
        Assertions.assertThrows(Exception.class, () -> httpClient11.send(request, HttpResponse.BodyHandlers.ofString()));
        Assertions.assertThrows(Exception.class, () -> httpClientSsl3.send(request, HttpResponse.BodyHandlers.ofString()));
        Assertions.assertThrows(Exception.class, () -> httpClientSsl2.send(request, HttpResponse.BodyHandlers.ofString()));
    }

    private String toString(List<String> value) {
        return String.join("", value);
    }
}
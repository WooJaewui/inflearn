package tobyspring.helloboot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class HelloApiTest {

    @Test
    void helloApi() {
        // http localhost:8080/hello?name=Spring
        // Httpie 에이치티티파이
        TestRestTemplate restTemplate = new TestRestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/hello?name={name}", String.class, "Spring");


        // status code 200
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // header(content-type) text/plain
        assertThat(response.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE)).startsWith(MediaType.TEXT_PLAIN_VALUE);

        // body Hello Spring
        assertThat(response.getBody()).isEqualTo("*hello Spring*");

    }

    @Test
    void failHelloAip() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/hello?name=", String.class, "Spring");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }


}

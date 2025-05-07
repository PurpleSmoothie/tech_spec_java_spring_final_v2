package subscription.app.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import subscription.app.demo.dto.subscription.SubscriptionDto;
import subscription.app.demo.dto.subscription.SubscriptionResponseDto;
import subscription.app.demo.dto.subscription.TopSubscriptionDto;
import subscription.app.demo.dto.user.UserDto;
import subscription.app.demo.dto.user.UserResponseDto;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubscriptionIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    private static Long userId1;
    private static Long userId2;
    private static Long netflixId;
    private static Long spotifyId;
    private static Long youtubeId;

    private String baseUrl(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    @Order(1)
    void createUsers() {
        UserDto user1 = new UserDto("Alice", "alice@example.com");
        UserDto user2 = new UserDto("Bob", "bob@example.com");

        ResponseEntity<UserResponseDto> resp1 = rest.postForEntity(baseUrl("/users"), user1, UserResponseDto.class);
        ResponseEntity<UserResponseDto> resp2 = rest.postForEntity(baseUrl("/users"), user2, UserResponseDto.class);

        assertThat(resp1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp2.getStatusCode()).isEqualTo(HttpStatus.OK);

        userId1 = resp1.getBody().id();
        userId2 = resp2.getBody().id();
    }

    @Test
    @Order(2)
    void addSubscriptions() {
        netflixId = addSubscription(userId1, "Netflix");
        spotifyId = addSubscription(userId1, "Spotify");
        youtubeId = addSubscription(userId1, "YouTube");

        addSubscription(userId2, "Netflix");
        addSubscription(userId2, "Spotify");
    }

    private Long addSubscription(Long userId, String name) {
        SubscriptionDto dto = new SubscriptionDto(name);
        ResponseEntity<SubscriptionResponseDto> response = rest.postForEntity(
                baseUrl("/users/" + userId + "/subscriptions"), dto, SubscriptionResponseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody().id();
    }

    @Test
    @Order(4)
    void getAllUserSubscriptions() {
        ResponseEntity<SubscriptionResponseDto[]> response = rest.getForEntity(
                baseUrl("/users/" + userId1 + "/subscriptions"), SubscriptionResponseDto[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(3);
    }

    @Test
    @Order(5)
    void updateSubscription() {
        SubscriptionDto updateDto = new SubscriptionDto("Netflix Premium");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SubscriptionDto> request = new HttpEntity<>(updateDto, headers);

        ResponseEntity<SubscriptionResponseDto> response = rest.exchange(
                baseUrl("/users/" + userId1 + "/subscriptions/" + netflixId),
                HttpMethod.PUT,
                request,
                SubscriptionResponseDto.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().name()).isEqualTo("Netflix Premium");
    }

    @Test
    @Order(6)
    void getSubscriptionById() {
        ResponseEntity<SubscriptionResponseDto> response = rest.getForEntity(
                baseUrl("/users/" + userId1 + "/subscriptions/" + netflixId), SubscriptionResponseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().name()).isEqualTo("Netflix Premium");
    }

    @Test
    @Order(3)
    void getTopSubscriptions() {
        ResponseEntity<TopSubscriptionDto[]> response = rest.getForEntity(
                baseUrl("/users/" + userId1 + "/subscriptions/top?limit=3"), TopSubscriptionDto[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<TopSubscriptionDto> top = Arrays.asList(response.getBody());

        assertThat(top).isNotEmpty();
        List<String> names = top.stream().map(TopSubscriptionDto::name).toList();
        List<Long> counts = top.stream().map(TopSubscriptionDto::count).toList();

        assertThat(names).contains("Netflix", "Spotify", "Netflix");

        assertThat(counts.stream().anyMatch(c -> c >= 2)).isTrue();
    }

    @Test
    @Order(7)
    void deleteSubscription() {
        rest.delete(baseUrl("/users/" + userId1 + "/subscriptions/" + youtubeId));
        ResponseEntity<SubscriptionResponseDto[]> response = rest.getForEntity(
                baseUrl("/users/" + userId1 + "/subscriptions"), SubscriptionResponseDto[].class);
        assertThat(response.getBody()).hasSize(2);
    }
}

package com.example.leetboardPro.Client;

import com.example.leetboardPro.DTO.LeetCodeRawData;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class LeetCodeClient {
    private final WebClient webClient;

    public LeetCodeClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://leetcode.com/graphql").build();
    }

    public LeetCodeRawData fetchUserData(String username) {
        String query = """
            query getUserProfile($username: String!) {
              matchedUser(username: $username) {
                submitStats {
                  acSubmissionNum {
                    difficulty
                    count
                  }
                }
              }
            }
            """;

        Map<String, Object> requestBody = Map.of(
                "query", query,
                "variables", Map.of("username", username)
        );

        return webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(LeetCodeRawData.class)
                .block(); // Synchronous call for simpler service logic
    }
}

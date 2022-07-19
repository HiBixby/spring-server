package com.twopow.security;

import com.twopow.security.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostApiControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void 주소가등록된다() throws Exception {
        Map<String, String> input = new HashMap<>();
        input.put("address", "부산");
        String url = "http://localhost:" + port + "/register";

        //when
        ResponseEntity<?> responseEntity = restTemplate.postForEntity(url, input);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        mvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                        .with(user(principalDetails))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());

        Optional<User> userOptional = userRepository.findByProviderAndProviderId(principalDetails.getUser().getProvider(), principalDetails.getUser().getProviderId());
        if (userOptional.isPresent()) {
            user = userOptional.get();
            assertThat(user.getAddress()).isEqualTo(input.get("address"));


        }
    }
}

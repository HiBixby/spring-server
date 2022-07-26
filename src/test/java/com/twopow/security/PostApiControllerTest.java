package com.twopow.security;

import com.twopow.security.jwt.JwtUtil;
import com.twopow.security.model.JwtTokens;
import com.twopow.security.model.Register;
import com.twopow.security.model.User;
import com.twopow.security.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostApiControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private String expiredAccessToken;
    private String accessToken;
    private String refreshToken;

    @Before
    public void setup() {
        refreshToken = JwtUtil.CreateToken(null,JwtUtil.Days(1));
        user = User.builder()
                .name("홍길동")
                .username("naver_123456789")
                .email("RedRoadCopper@naver.com")
                .role("ROLE_USER")
                .provider("naver")
                .providerId("123456789")
                .picture("https://ssl.pstatic.net/static/newsstand/up/2013/0813/nsd114029379.gif")
                .refreshToken(refreshToken)
                .build();
        userRepository.save(user);

        expiredAccessToken = JwtUtil.CreateToken(user,JwtUtil.Minutes(-1));
        accessToken = JwtUtil.CreateToken(user,JwtUtil.Minutes(1));
    }

    @After
    public void tearDown() throws Exception{
        userRepository.deleteAll();
    }
    @Test
    public void 주소가등록된다() throws Exception {

        //given
        String address = "주소";
        Register register = new Register();
        register.setAddress(address);

        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // build the request
        HttpEntity<Register> request = new HttpEntity<>(register,headers);

        String url = "http://localhost:"+port+"/register";

        //when
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url,request,String.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan("");

        List<User> all = userRepository.findAll();
        assertThat(all.get(0).getAddress()).isEqualTo(address);
    }

    @Test
    public void 리이슈된다() throws Exception {

        //given

        System.out.println("expiredToken: "+expiredAccessToken);
        JwtTokens jwtTokens = JwtTokens.builder().accessToken(expiredAccessToken).refreshToken(refreshToken).build();

        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // build the request
        HttpEntity<JwtTokens> request = new HttpEntity<>(jwtTokens,headers);

        String url = "http://localhost:"+port+"/auth/reissue";

        //when
        ResponseEntity<JwtTokens> responseEntity = restTemplate.postForEntity(url,request,JwtTokens.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        JwtTokens newJwtTokens = responseEntity.getBody();

        List<User> all = userRepository.findAll();
        assertThat(all.get(0).getRefreshToken()).isNotEqualTo(refreshToken);
        assertThat(all.get(0).getRefreshToken()).isEqualTo(newJwtTokens.getRefreshToken());

        //response로 받은 accessToken이 기존 accessToken과 다른지 검사
        assertThat(accessToken).isNotEqualTo(newJwtTokens.getAccessToken());
    }
}

package com.twopow.security.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JwtTokens {
    private final String accessToken;
    private final String refreshToken;
}

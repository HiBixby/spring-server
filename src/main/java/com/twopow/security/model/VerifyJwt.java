package com.twopow.security.model;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Builder
@Setter
public class VerifyJwt {
    String jwtToken;
    Boolean expired;
}

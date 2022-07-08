package com.twopow.security.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinedUser {
    private String userToken;
    private String username;
    private String email;
    private String profileImageUrl;
}

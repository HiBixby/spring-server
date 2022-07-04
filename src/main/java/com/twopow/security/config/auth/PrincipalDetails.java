package com.twopow.security.config.auth;
//시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
//로그인 진행 완료가 되면 시큐리티 session을 만들어준다.
//오브젝트=>Authentication 타입 객체
//Authentication 안에 User정보가 있어야 됨.
//user 오브젝트 타입=>UserDetails 타입 객체.

import com.twopow.security.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

//Security Session=>Authentication=>UserDetails
@Data
public class PrincipalDetails implements UserDetails,OAuth2User {
    private User user;//콤포지션
    private Map<String,Object> attributes;
    
    //일반로그인
    public PrincipalDetails(User user){
        this.user=user;
    }
    //해당 유저의 권한을 리턴하는곳

    //OAuth로그인인
   public PrincipalDetails(User user,Map<String,Object>attributes){
        this.user=user;
        this.attributes=attributes;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    //아래 2개(getAttributes,getName)은 OAuth2User implements 해서 생긴 메서드
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        //return attributes.get("sub"); 라고 하면 되지만 안중요해서 그냥 null리턴하기로 함
        return null;
    }
}

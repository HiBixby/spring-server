package com.twopow.security.config.auth;

import com.twopow.security.model.User;
import com.twopow.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthInfoService {
    private final UserRepository userRepository;
    @Transactional
    public User 주소저장(PrincipalDetails principalDetails, String address){
        Optional<User> userOptional;
        User user;
        userOptional = userRepository.findByProviderAndProviderId(principalDetails.getUser().getProvider(),principalDetails.getUser().getProviderId());
        if(userOptional.isPresent()){
            user=userOptional.get();
            user.setAddress(address);
            return userRepository.save(user);
        }
        return null;
    }
}

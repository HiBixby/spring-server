package com.twopow.security.config.auth;

import com.twopow.security.model.User;
import com.twopow.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthInfoService {
    private final UserRepository userRepository;
    @Transactional
    public String 주소저장(HttpServletRequest request, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("프론트에서 받은 주소 : "+request.getParameter("address"));
        String address = request.getParameter("address");
        Optional<User> userOptional;
        User user;
        userOptional = userRepository.findByProviderAndProviderId(principalDetails.getUser().getProvider(),principalDetails.getUser().getProviderId());
        if(userOptional.isPresent()){
            user=userOptional.get();
            user.setAddress(address);
            userRepository.save(user);
            return address;
        }
        return null;
    }
}

package com.twopow.security.controller;

import com.twopow.security.model.User;
import com.twopow.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller//view를 리턴
public class IndexController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    //localhost:8080
    //localhost:8080/
    @GetMapping({"","/"})
    public String index(){
        //머스테치 기본 폴더 src/main/resources/
        //뷰리졸버설정:templates(prefix),.mustache(suffix)
        return "index";//src/main/resources/templates/index.mustache
    }
    @GetMapping("/user")
    public @ResponseBody String user(){

         return "user";
    }
    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }
    @GetMapping("/manager")
    public @ResponseBody String manager(){

        return "manager";
    }
    @GetMapping("/loginForm")
    public String loginForm(){

        return "loginForm";
    }
    @GetMapping("/joinForm")
    public String joinForm(){

        return "joinForm";
    }
    @PostMapping("/join")
    public String join(User user){
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword=user.getPassword();
        String encPassword=bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user);//회원가입 잘되지만 비밀번호 암호화 안돼서 시큐리티로 로그인 불가.
        return "redirect:/loginForm";
    }
    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "데이터";
    }
}
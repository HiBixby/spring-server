//package com.twopow.security.controller;
//
//import com.twopow.security.config.auth.PrincipalDetails;
//import com.twopow.security.model.User;
//import com.twopow.security.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.annotation.Secured;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//@CrossOrigin(origins = "http://localhost:3000")
//@Controller//view를 리턴
//public class IndexController {
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    @GetMapping("/test/login")
//    public @ResponseBody String testLogin(Authentication authentication /*, @AuthenticationPrincipal UserDetails userDetails*/ ,
//                                          @AuthenticationPrincipal PrincipalDetails userDetails){
//        //DI(의존성주입), PrincipalDetails가 UserDetails를 implements해서 이렇게 쓰기 가능
//        System.out.println("/test/login/==========");
//        PrincipalDetails principalDetails=(PrincipalDetails)authentication.getPrincipal();//다운캐스팅
//        System.out.println("authentication:"+principalDetails.getUser());
//        //System.out.println("userDetails:"+userDetails.getUsername());
//        System.out.println("userDetails:"+userDetails.getUser());
//        return "세션정보 확인하기";
//    }
//
//    @GetMapping("/test/oauth/login")
//    public @ResponseBody String testOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth){
//        //Authentication 객체로도 접근 가능하고 @AuthenticationPrincipal 어노테이션으로도 접근 가능! 대신 타입이 OAuth2User type이다.
//        //DI(의존성주입)
//        System.out.println("/test/oauth/login/==========");
//        OAuth2User oauth2User =(OAuth2User) authentication.getPrincipal();//다운캐스팅
//
//        System.out.println("authentication:"+oauth2User.getAttributes());
//        System.out.println("oauth2User:"+oauth.getAttributes());
//        return "OAuth 세션정보 확인하기";
//    }
//
//    //localhost:8080
//    //localhost:8080/
//    @GetMapping({"","/"})
//    public String index(){
//        //머스테치 기본 폴더 src/main/resources/
//        //뷰리졸버설정:templates(prefix),.mustache(suffix)
//        return "index";//src/main/resources/templates/index.mustache
//    }
//
//    //Oauth 로그인 해도 PrincipalDetails
//    //일반 로그인을 해도 PrincipalDetails
//    @GetMapping("/user")
//    //@AuthenticationPrincipal 어노테이션으로 접근하면 다운캐스팅 안하고 바로 접근 가능
//    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
//        System.out.println("principalDetails:"+principalDetails.getUser());
//         return "user";
//    }
//    @GetMapping("/admin")
//    public @ResponseBody String admin(){
//        return "admin";
//    }
//    @GetMapping("/manager")
//    public @ResponseBody String manager(){
//
//        return "manager";
//    }
//    @GetMapping("/loginForm")
//    public String loginForm(){
//
//        return "loginForm";
//    }
//    @GetMapping("/joinForm")
//    public String joinForm(){
//
//        return "joinForm";
//    }
//    @PostMapping("/join")
//    public String join(User user){
//        System.out.println(user);
//        user.setRole("ROLE_USER");
//        String rawPassword=user.getPassword();
//        String encPassword=bCryptPasswordEncoder.encode(rawPassword);
//        user.setPassword(encPassword);
//        userRepository.save(user);//회원가입 잘되지만 비밀번호 암호화 안돼서 시큐리티로 로그인 불가.
//        return "redirect:/loginForm";
//    }
//    @Secured("ROLE_ADMIN")
//    @GetMapping("/info")
//    public @ResponseBody String info(){
//        return "개인정보";
//    }
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
//    @GetMapping("/data")
//    public @ResponseBody String data(){
//        return "데이터";
//    }
//}
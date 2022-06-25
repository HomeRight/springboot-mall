package com.alensu.springbootmall.controller;

import com.alensu.springbootmall.dto.UserLoginRequest;
import com.alensu.springbootmall.dto.UserRegisterRequest;
import com.alensu.springbootmall.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;


@Controller
public class TymeleafController {

    @GetMapping("/home")
    public String home(Model model) {
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        model.addAttribute("userLoginRequest", userLoginRequest);
        model.addAttribute("userRegisterRequest", userRegisterRequest);
        return "index";
    }


    @GetMapping("/welcome")
    public String welcome(Model model) {
//        UserLoginRequest userLoginRequest = new UserLoginRequest();
//        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
//        model.addAttribute("userLoginRequest", userLoginRequest);
//        model.addAttribute("userRegisterRequest", userRegisterRequest);
        return "welcome";
    }

    @GetMapping("/travel")
    public String travel(Model model) {
//        UserLoginRequest userLoginRequest = new UserLoginRequest();
//        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
//        model.addAttribute("userLoginRequest", userLoginRequest);
//        model.addAttribute("userRegisterRequest", userRegisterRequest);
        return "travel";
    }


    @GetMapping("/logout")
    public String logout(Model model) {
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        model.addAttribute("userLoginRequest", userLoginRequest);
        model.addAttribute("userRegisterRequest", userRegisterRequest);
        return "index";
    }


    @PostMapping("/login")
    public String login(@ModelAttribute UserLoginRequest userLoginRequest, HttpServletRequest req) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String userLoginRequestStr = objectMapper.writeValueAsString(userLoginRequest);
        System.out.println("userLoginRequestStr=" + userLoginRequestStr);


        try {

            RestTemplate restTemplate = new RestTemplate();
            URI uri = ServletUriComponentsBuilder.fromContextPath(req).path("/users/login").build().toUri();
            ResponseEntity<User> userResponseEntity = restTemplate.postForEntity(uri, userLoginRequest, User.class);

            String result = objectMapper.writeValueAsString(userResponseEntity);
            System.out.println("result=" + result);

            System.out.println(userResponseEntity.getStatusCode());

            if (userResponseEntity.getStatusCode().equals(HttpStatus.OK)) {

                System.out.println("成功登入");
                return "redirect:/welcome";
            } else {
                return "loginFail";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "loginFail";
        }


    }


    @PostMapping("/register")
    public String register(@ModelAttribute UserRegisterRequest userRegisterRequest, HttpServletRequest req) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String userRegisterRequestStr = objectMapper.writeValueAsString(userRegisterRequest);
        System.out.println("userRegisterRequestStr=" + userRegisterRequestStr);


        try {

            RestTemplate restTemplate = new RestTemplate();
            URI uri = ServletUriComponentsBuilder.fromContextPath(req).path("/users/register").build().toUri();
            ResponseEntity<User> userResponseEntity = restTemplate.postForEntity(uri, userRegisterRequest, User.class);

            String result = objectMapper.writeValueAsString(userResponseEntity);
            System.out.println("result=" + result);

            System.out.println(userResponseEntity.getStatusCode());

            if (userResponseEntity.getStatusCode().equals(HttpStatus.CREATED)) {

                System.out.println("註冊成功");
                return "redirect:/welcome";
            } else {
                return "loginFail";
            }
        } catch (Exception e) {

            System.out.println("註冊失敗");
            e.printStackTrace();
            return "loginFail";
        }


    }


}

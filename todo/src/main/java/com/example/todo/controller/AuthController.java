package com.example.todo.controller;

import com.example.todo.domain.User;
import com.example.todo.repository.UserRepository;
import com.example.todo.security.PasswordUtil;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping("/signup")
    public void signup(@RequestBody SignupRequest request) {
        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }
        String hashedPassword = PasswordUtil.hashPassword(request.getPassword());
        User user = new User(request.getUsername(), hashedPassword);
        userRepository.save(user);
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest request, HttpSession session) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String hashedPassword = PasswordUtil.hashPassword(request.getPassword());
        if(!user.getPassword().equals(hashedPassword)){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        session.setAttribute("loginUser", user.getId());

        // 추가: 로그인 후 서버 콘솔에 세션 ID 출력
        System.out.println("로그인 성공 - 세션 ID: " + session.getId());

        //추가
        System.out.println("로그인 성공 - 유저 ID: " + user.getId());

    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }

    //확인용 임시 api
    @GetMapping("/check")
    public String checkSession(HttpSession session) {

        System.out.println("현재 요청의 세션 ID: " + session.getId());  // 추가

        System.out.println("현재 세션 loginUser 속성: " + session.getAttribute("loginUser"));  // 추가

        Object userId = session.getAttribute("loginUser");
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not logged in");
        }
        return "현재 로그인된 사용자 ID: " + userId;
    }
}

@Data
class SignupRequest {
    private String username;
    private String password;
}

@Data
class LoginRequest {
    private String username;
    private String password;
}
package com.dompoo.onlineshopping.service;

import com.dompoo.onlineshopping.domain.User;
import com.dompoo.onlineshopping.exception.userException.AlreadyExistsEmailException;
import com.dompoo.onlineshopping.repository.UserRepository;
import com.dompoo.onlineshopping.request.auth.SignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * <pre>
     * 설명 :
     * 회원가입을 합니다.
     *
     * 동작 :
     * SignupRequest 클래스를 파라미터로 받고,
     * email이 중복이라면 예외를 터트리고,
     * 그 외의 경우에는 password를 인코딩하여 레포지토리에 저장합니다.
     */
    public void signup(SignupRequest request) {
        Optional<User> findUsers = userRepository.findByEmail(request.getEmail());
        if (findUsers.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        User user = User.builder()
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .build();
        userRepository.save(user);
    }
}

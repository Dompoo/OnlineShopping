package com.dompoo.onlineshopping.service;

import com.dompoo.onlineshopping.domain.User;
import com.dompoo.onlineshopping.exception.userException.AlreadyExistsEmailException;
import com.dompoo.onlineshopping.repository.UserRepository;
import com.dompoo.onlineshopping.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public void signup(SignupRequest request) {
        Optional<User> findUsers = userRepository.findByEmail(request.getEmail());
        if (findUsers.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        User user = User.builder()
                .name(request.getName())
                .password(request.getPassword())
                .email(request.getEmail())
                .build();
        userRepository.save(user);
    }
}

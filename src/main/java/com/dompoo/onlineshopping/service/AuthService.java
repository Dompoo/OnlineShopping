package com.dompoo.onlineshopping.service;

import com.dompoo.onlineshopping.crypto.PasswordEncoder;
import com.dompoo.onlineshopping.domain.Users;
import com.dompoo.onlineshopping.exception.userException.AlreadyExistsEmailException;
import com.dompoo.onlineshopping.exception.userException.InvalidSigninInfo;
import com.dompoo.onlineshopping.repository.UserRepository;
import com.dompoo.onlineshopping.request.LoginRequest;
import com.dompoo.onlineshopping.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Transactional
    public Long signin(LoginRequest request) {
        Users findUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidSigninInfo::new);

        if (!encoder.matches(request.getPassword(), findUser.getPassword())) {
            throw new InvalidSigninInfo();
        }

        findUser.addSession();

        return findUser.getId();
    }

    public void signup(SignupRequest request) {
        Optional<Users> findUsers = userRepository.findByEmail(request.getEmail());
        if (findUsers.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        String encryptedPassword = encoder.encrypt(request.getPassword());

        Users users = Users.builder()
                .name(request.getName())
                .password(encryptedPassword)
                .email(request.getEmail())
                .build();
        userRepository.save(users);
    }
}

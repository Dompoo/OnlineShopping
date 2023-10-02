package com.dompoo.onlineshopping.service;

import com.dompoo.onlineshopping.domain.Session;
import com.dompoo.onlineshopping.domain.Users;
import com.dompoo.onlineshopping.exception.userException.AlreadyExistsEmailException;
import com.dompoo.onlineshopping.exception.userException.InvalidSigninInfo;
import com.dompoo.onlineshopping.repository.SessionRepository;
import com.dompoo.onlineshopping.repository.UserRepository;
import com.dompoo.onlineshopping.request.LoginRequest;
import com.dompoo.onlineshopping.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    @Transactional
    public Long signin(LoginRequest request) {
        // DB에서 조회
        Users findUser = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(InvalidSigninInfo::new);

        Session session = findUser.addSession();

        return findUser.getId();
    }

    public void signup(SignupRequest request) {
        Optional<Users> findUsers = userRepository.findByEmail(request.getEmail());
        if (findUsers.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        SCryptPasswordEncoder encoder = new SCryptPasswordEncoder(16, 8, 1, 32, 64);
        String encryptedPassword = encoder.encode(request.getPassword());

        Users users = Users.builder()
                .name(request.getName())
                .password(encryptedPassword)
                .email(request.getEmail())
                .build();
        userRepository.save(users);
    }
}

package com.dompoo.onlineshopping.service;

import com.dompoo.onlineshopping.domain.Session;
import com.dompoo.onlineshopping.domain.Users;
import com.dompoo.onlineshopping.exception.InvalidSigninInfo;
import com.dompoo.onlineshopping.repository.SessionRepository;
import com.dompoo.onlineshopping.repository.UserRepository;
import com.dompoo.onlineshopping.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    @Transactional
    public String signin(LoginRequest request) {
        // DB에서 조회
        Users findUser = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(InvalidSigninInfo::new);

        Session newSession = Session.builder()
                .users(findUser)
                .build();

        Session session = findUser.addSession(newSession);
        sessionRepository.save(newSession);

        return session.getAccessToken();
    }
}

package com.dompoo.onlineshopping.config;

import com.dompoo.onlineshopping.exception.Unauthorized;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = request.getParameter("accessToken");
        if (accessToken != null && accessToken.equals("dompoo")) {
            request.setAttribute("userName", accessToken);
            return true;
        }
        throw new Unauthorized();
    }
}

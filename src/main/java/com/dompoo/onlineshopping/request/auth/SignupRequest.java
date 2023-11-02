package com.dompoo.onlineshopping.request.auth;

import lombok.Builder;
import lombok.Data;

@Data
public class SignupRequest {

    private String name;
    private String password;
    private String email;

    @Builder
    public SignupRequest(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }
}

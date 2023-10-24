package com.dompoo.onlineshopping.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MyMockSecurityContext.class)
public @interface MyMockUser {

    String name() default "dompoo";

    String email() default "dompoo@gmail.com";

    String password() default "1234";

}

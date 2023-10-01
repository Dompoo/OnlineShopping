package com.dompoo.onlineshopping.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "dompoo")
public class AppConfig {

    public String hello;
}

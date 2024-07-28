package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
public class TokenConfig {

    @Bean
    @RequestScope
    public TokenHolder tokenHolder() {
        return new TokenHolder();
    }

    public static class TokenHolder {
        private String jwtToken;

        public String getJwtToken() {
            return jwtToken;
        }

        public void setJwtToken(String jwtToken) {
            this.jwtToken = jwtToken;
        }
    }
}

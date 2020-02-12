package com.example.manageinventory.authentication;

public class TokenResponse {
    private String jwt;

    public void TokenReponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public TokenResponse(String jwt) {
        this.jwt = jwt;
    }
}

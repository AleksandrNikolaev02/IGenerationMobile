package com.example.igenerationmobile.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {
    @JsonProperty("access_token")
    private String access_token;
    @JsonProperty("token_type")
    private String token_type;
    @JsonProperty("expires_at")
    private String expires_at;

    public Token(){}

    public Token(String access_token, String token_type, String expires_at) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.expires_at = expires_at;
    }

    public String getTokenType() {
        return token_type;
    }

    public String getAccessToken() {
        return access_token;
    }

    public String getExpiresAt() {
        return expires_at;
    }

    @Override
    public String toString() {
        return "{" +
                "\"access_token\":\"" + access_token + '\"' +
                ", \"token_type\":\"" + token_type + '\"' +
                ", \"expires_at\":\"" + expires_at + '\"' +
                '}';
    }
}

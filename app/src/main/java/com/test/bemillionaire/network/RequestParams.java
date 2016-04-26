package com.test.bemillionaire.network;


import java.util.HashMap;

public class RequestParams {
    private String login;
    private String password;
    private String grantType;
    private String tokenType;
    private String accessToken;
    private String refreshToken;
    private String url;
    private String typeRequest;
    private String userId;
    private HashMap<String, String> params;


    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTypeRequest() {
        return typeRequest;
    }

    public void setTypeRequest(String typeRequest) {
        this.typeRequest = typeRequest;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTokentype() {
        return tokenType;
    }

    public void setTokentype(String tokentype) {
        this.tokenType = tokentype;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}

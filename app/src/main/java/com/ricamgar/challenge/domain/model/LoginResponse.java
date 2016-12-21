package com.ricamgar.challenge.domain.model;


public class LoginResponse {

    public final String _id;
    public final String token_type;
    public final String access_token;
    public final String description;
    public final String scope;
    public final String grant_type;
    public final App app;
    public final String created_at;
    public final String updated_at;

    public LoginResponse(String id, String token_type, String access_token, String description,
                         String scope, String grant_type, App app, String created_at,
                         String updated_at) {
        _id = id;
        this.token_type = token_type;
        this.access_token = access_token;
        this.description = description;
        this.scope = scope;
        this.grant_type = grant_type;
        this.app = app;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    private class App {
        public final String client_id;
        public final String name;
        public final String homepage_url;

        private App(String client_id, String name, String homepage_url) {
            this.client_id = client_id;
            this.name = name;
            this.homepage_url = homepage_url;
        }
    }
}

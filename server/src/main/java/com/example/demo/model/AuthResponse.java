package com.example.demo.model;

public class AuthResponse {
    private final long id;
    private final String username;
    private final String token;
    private final String role;
    private final String email;
    
    public AuthResponse(long id, String username,String token, String role, String email) {
        this.id = id;
        this.username = username;
        this.token = token;
        this.role = role;
        this.email = email;
    }

    public long getId(){
        return id;
    }

    public String getUsername(){
        return username;
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }
}
package com.example.security.dtos;

public class RefreshToken {
    private String token;
    
    public RefreshToken() { }
	public RefreshToken(String token) {
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}
    
}

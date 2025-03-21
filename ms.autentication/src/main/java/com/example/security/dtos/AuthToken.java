package com.example.security.dtos;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@SuppressWarnings("serial")
@JsonInclude(value = Include.NON_EMPTY)
public class AuthToken implements Serializable {
	private boolean success = false;
    private String token;
    private String refresh;
    private String name;
    private List<String> roles;
    private int expires_in;
    
    public AuthToken() { }
	public AuthToken(boolean success, String token, String refresh, String name, List<String> roles, int expires_in) {
		this.success = success;
		this.token = token;
		this.refresh = refresh;
		this.name = name;
		this.roles = roles;
		this.expires_in = expires_in;
	}
	
	public boolean isSuccess() {
		return success;
	}
	public String getToken() {
		return token;
	}
	public String getRefresh() {
		return refresh;
	}
	public String getName() {
		return name;
	}
	public List<String> getRoles() {
		return roles;
	}
	public int getExpires_in() {
		return expires_in;
	}
    
}

package com.techelevator.tenmo.models;

import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AuthenticatedUser {
	
	private String token;
	private User user;
	private BigDecimal balance;
	public RestTemplate restTemplate = new RestTemplate();
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public BigDecimal getBalance() {
		return balance;
	}
}

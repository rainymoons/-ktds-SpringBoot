package com.ktdsuniversity.edu.hello_spring.member.vo;

import java.util.List;

public class MemberVO {

	private String email;
	private String name;
	private String password;
	private String salt;
	private int loginFailCount;
	private String latestLoginFailDate;
	private String latestLoginIp;
	private String latestLoginSuccessDate;
	private String picture;
	/**
	 * OAauth2.0 channel
	 */
	private String provider;
	
	/**
	 * 사용자의 역할 (하나의 역할은 여러 권한을 가짐)
	 */
	private String role;
	
	/**
	 * 사용자의 권한.
	 */
	private List<AuthorityVO> Authority;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public int getLoginFailCount() {
		return loginFailCount;
	}
	public void setLoginFailCount(int loginFailCount) {
		this.loginFailCount = loginFailCount;
	}
	public String getLatestLoginFailDate() {
		return latestLoginFailDate;
	}
	public void setLatestLoginFailDate(String latestLoginFailDate) {
		this.latestLoginFailDate = latestLoginFailDate;
	}
	public String getLatestLoginIp() {
		return latestLoginIp;
	}
	public void setLatestLoginIp(String latestLoginIp) {
		this.latestLoginIp = latestLoginIp;
	}
	public String getLatestLoginSuccessDate() {
		return latestLoginSuccessDate;
	}
	public void setLatestLoginSuccessDate(String latestLoginSuccessDate) {
		this.latestLoginSuccessDate = latestLoginSuccessDate;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public List<AuthorityVO> getAuthority() {
		return Authority;
	}
	public void setAuthority(List<AuthorityVO> authority) {
		Authority = authority;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
}
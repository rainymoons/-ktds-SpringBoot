package com.ktdsuniversity.edu.hello_spring.bbs.vo;
/**
 * 클라이언트가 요청한 form 파라미터를 받아오기 위한 VO
 */
public class WriteBoardVO {

	private String subject;
	private String email;
	private String content;
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}

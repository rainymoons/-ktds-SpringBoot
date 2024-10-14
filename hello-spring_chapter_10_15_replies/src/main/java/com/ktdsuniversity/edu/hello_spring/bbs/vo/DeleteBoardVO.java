package com.ktdsuniversity.edu.hello_spring.bbs.vo;

public class DeleteBoardVO {

	private int id;
	/*
	 * 게시글을 이 사람이 삭제 요청을 했다.
	 */
	private String email;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}

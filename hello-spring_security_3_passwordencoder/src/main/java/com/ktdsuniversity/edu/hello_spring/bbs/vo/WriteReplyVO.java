package com.ktdsuniversity.edu.hello_spring.bbs.vo;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

public class WriteReplyVO {

	private int replyId;
	private int boardId;
	private String email;
	
	@Length(min=5, message="댓글의 내용은 다섯글자 이상 입력해주세요.")
	@NotBlank(message="댓글의 내용을 입력하세요.")
	private String content;
	
	private int parentReplyId; // 0 이면 일반 댓글. 0이 아니면 대댓글
	
	public int getReplyId() {
		return replyId;
	}
	public void setReplyId(int replyId) {
		this.replyId = replyId;
	}
	public int getBoardId() {
		return boardId;
	}
	public int getParentReplyId() {
		return parentReplyId;
	}
	public void setParentReplyId(int parentReplyId) {
		this.parentReplyId = parentReplyId;
	}
	public void setBoardId(int boardId) {
		this.boardId = boardId;
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

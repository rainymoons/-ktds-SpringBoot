package com.ktdsuniversity.edu.hello_spring.bbs.vo;

import org.springframework.web.multipart.MultipartFile;

/**
 * 클라이언트가 요청한 form 파라미터를 받아오기 위한 VO
 */
public class WriteBoardVO {

	// 필수 입력값. ->비어있나 검증. 언제? 서비스를 호출하기 전에
	private String subject;
	private String email;
	private String content;
	
	/**
	 *  사용자가 전송한 파일을 받아온다. 여기서 받아오므로 controller에서 따로 받아올 일이 없어진다.
	 */
	private MultipartFile file;
	
	/**
	 * 데이터베이스에 넣어주기 위한 변수 추가. BoardVO에 존재하는 변수(Mapper - insertNewBoard)
	 */
	private String originFileName;
	
	/**
	 * originFileName과 fileName을 난독화 제목, 일반 제목으로 분리시키기 위한 변수 선언.
	 */
	private String fileName;
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
		
		// 파라미터가 들어올때부터 trim()을 사용해서 공백이 사라짐.(좌우공백)
		if (this.subject != null) {
			this.subject = this.subject.trim();
			// xxs 방어
			// <를 less than ;으로 바꿔라.
			this.subject = this.subject.replace("<", "&lt;");
			this.subject = this.subject.replace(">", "&gt;");
		}
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
		
		if (this.email != null) {
			this.email = this.email.trim();
			// xxs 방어
			// <를 less than ;으로 바꿔라.
			this.email = this.email.replace("<", "&lt;");
			this.email = this.email.replace(">", "&gt;");	
		}
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
		
		if (this.content != null) {
			this.content = this.content.trim();
			// xxs 방어
			// <를 less than ;으로 바꿔라.
			this.content = this.content.replace("<", "&lt;");
			this.content = this.content.replace(">", "&gt;");
		}
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getOriginFileName() {
		return originFileName;
	}
	public void setOriginFileName(String originFileName) {
		this.originFileName = originFileName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}

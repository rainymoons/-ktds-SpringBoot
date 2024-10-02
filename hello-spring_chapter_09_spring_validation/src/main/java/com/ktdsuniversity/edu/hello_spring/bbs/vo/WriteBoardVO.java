package com.ktdsuniversity.edu.hello_spring.bbs.vo;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 클라이언트가 요청한 form 파라미터를 받아오기 위한 VO
 */
public class WriteBoardVO {

	// validation check 
	
	@NotBlank(message = "제목은 필수 입력 값입니다.") // 공백, null 체크
	@Size(min = 5, max = 100, message = "제목은 5~100글자 내로 입력해주세요") // 5 ~ 100 글자
	private String subject;
	
	@NotBlank(message = "이메일은 필수 입력 값입니다.")
	@Email(message = "올바른 이메일 형식으로 입력해주세요.")
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

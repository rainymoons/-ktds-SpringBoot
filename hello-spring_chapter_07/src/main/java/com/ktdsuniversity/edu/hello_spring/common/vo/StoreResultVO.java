package com.ktdsuniversity.edu.hello_spring.common.vo;
/**
 * 난독화된 파일의 이름과 난독화되지 않은 파일의 이름을 관리하기 위한 VO
 */
public class StoreResultVO {

	
	private String originFileName;
	private String obfuscatedFileName;
	
	// 파일 명이 한번 결정되면 절대로 바뀌면 안된다.setter없이 생성자로 만든다. 외부에서 데이터를 수정하지 못하도록 만들어야 함.
	public StoreResultVO(String originFileName, String obfuscatedFileName) {
		super();
		this.originFileName = originFileName;
		this.obfuscatedFileName = obfuscatedFileName;
	}
	
	public String getOriginFileName() {
		return originFileName;
	}
	public String getObfuscatedFileName() {
		return obfuscatedFileName;
	}
}

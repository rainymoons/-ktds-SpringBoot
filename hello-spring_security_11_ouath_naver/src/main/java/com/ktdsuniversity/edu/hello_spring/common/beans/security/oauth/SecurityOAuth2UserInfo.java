package com.ktdsuniversity.edu.hello_spring.common.beans.security.oauth;

import java.util.Map;

/**
 * OAuth2.0 인증을 한 사용자의 정보 ( scope : name, email )를 받아올 인터페이스
 * 인터페이스로 만드는 이유?
 *  - Naver만 사용할 경우 => Interface를 사용할 이유가 없음
 *  - Naver, Google, Facebook, Github, Kakao, Instagram, Reddit 등을 모두 이용할 경우
 *  		=> scope 데이터를 가져오는 방법이 다 다르다.
 */
public interface SecurityOAuth2UserInfo {
	
	/**
	 * oAuth 인증이 완료된 사용자의 scope 데이터를 돌려준다. ( JSON -> Map )
	 * @return
	 */
	public Map<String, Object> getScope();
	
	
	/**
	 * oAuth 인증이 완료된 사용자의 scope에서 이름 정보를 돌려준다.
	 * @return
	 */
	public String getNameInScope();
	
	/**
	 * oAuth 인증이 완료된 사용자의 scope에서 이메일 정보를 돌려준다.
	 * @return
	 */
	public String getEmailInScope();
	
	public default String getProfileImageInScope() {
		return null;
	}
}


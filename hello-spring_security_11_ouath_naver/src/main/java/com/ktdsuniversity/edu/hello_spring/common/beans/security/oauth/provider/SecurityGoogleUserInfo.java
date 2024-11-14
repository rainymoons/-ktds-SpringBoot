package com.ktdsuniversity.edu.hello_spring.common.beans.security.oauth.provider;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktdsuniversity.edu.hello_spring.common.beans.security.oauth.SecurityOAuth2UserInfo;

/**
 * 'Google' OAuth2.0의 결과에서 사용자 정보를 취득하는 클래스
 *  데이터 응답 형태
 *   {
 *   	response: {
 *   	  profile: "userId",
 *   	  email: "userEmail@google.com",
 *   	  openid: ~~~
 *   	}
 *   }
 *   
 *   getScope에서 resonse를 돌려주고 resopnse에서 이름과 이메일을 가져와야 함.
 */
public class SecurityGoogleUserInfo implements SecurityOAuth2UserInfo {

	private static final Logger log = LoggerFactory.getLogger(SecurityGoogleUserInfo.class);
	
	// response 데이터 저장
	private Map<String, Object> scope;
	
	// 데이터를 넣어줄 생성자 생성
	@SuppressWarnings("unchecked")
	public SecurityGoogleUserInfo(Map<String, Object> scope) { // scope안에 JSON 데이터가 있음. 여기서 name과 email만 떼서 scope에 넣어줌.
		log.debug("구글 Scope: {}", scope.toString());
		
		this.scope = scope; 
	}
	
	@Override
	public Map<String, Object> getScope() {
		return this.scope;
	}

	@Override
	public String getNameInScope() { // profile에서 name을 꺼내야함.
		return this.scope.get("name").toString();
	}

	@Override
	public String getEmailInScope() {
		return this.scope.get("email").toString();
	}
	
	@Override
	public String getProfileImageInScope() {
		return this.scope.get("picture").toString();
	}
}

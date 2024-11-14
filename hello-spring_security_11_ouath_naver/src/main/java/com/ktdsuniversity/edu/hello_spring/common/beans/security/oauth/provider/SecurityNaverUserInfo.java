package com.ktdsuniversity.edu.hello_spring.common.beans.security.oauth.provider;

import java.util.Map;

import com.ktdsuniversity.edu.hello_spring.common.beans.security.oauth.SecurityOAuth2UserInfo;

/**
 * 'Naver' OAuth2.0의 결과에서 사용자 정보를 취득하는 클래스
 *  데이터 응답 형태
 *   {
 *   	response: {
 *   	  name: "userId",
 *   	  email: "userEmail@naver.com"
 *   	}
 *   }
 *   
 *   getScope에서 resonse를 돌려주고 resopnse에서 이름과 이메일을 가져와야 함.
 */
public class SecurityNaverUserInfo implements SecurityOAuth2UserInfo {

	// response 데이터 저장
	private Map<String, Object> scope;
	
	// 데이터를 넣어줄 생성자 생성
	@SuppressWarnings("unchecked")
	public SecurityNaverUserInfo(Map<String, Object> scope) { // scope안에 JSON 데이터가 있음. 여기서 name과 email만 떼서 scope에 넣어줌.
		this.scope = (Map<String, Object>) scope.get("response"); // casting 필요
	}
	
	@Override
	public Map<String, Object> getScope() {
		return this.scope;
	}

	@Override
	public String getNameInScope() {
		return this.scope.get("name").toString();
	}

	@Override
	public String getEmailInScope() {
		return this.scope.get("email").toString();
	}
}

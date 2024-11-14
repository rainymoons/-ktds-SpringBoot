package com.ktdsuniversity.edu.hello_spring.common.beans.security.oauth;

import java.util.Map;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.ktdsuniversity.edu.hello_spring.common.beans.security.SecurityUser;
import com.ktdsuniversity.edu.hello_spring.member.vo.MemberVO;

public class SecurityOAuthUserDetails extends SecurityUser implements OAuth2User {

	private static final long serialVersionUID = -2568567637048397275L; // SecurityUser때문에 생김

	private SecurityOAuth2UserInfo securityOAuth2UserInfo;
	
	// 파라미터가 있는 생성자는 따로 호출
	public SecurityOAuthUserDetails(MemberVO memberVO, SecurityOAuth2UserInfo securityOAuth2UserInfo) { //SecurityOAuth2UserInfo는 scope 데이터들
		super(memberVO);
		this.securityOAuth2UserInfo = securityOAuth2UserInfo;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return this.securityOAuth2UserInfo.getScope();
	}

	/**
	 * 사용자의 이름
	 */
	@Override
	public String getName() {
		return super.getMemberVO().getName();
	}
	
	public String getEmail() {
		return super.getMemberVO().getEmail();
	}
}

package com.ktdsuniversity.edu.hello_spring.common.beans.security.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.ktdsuniversity.edu.hello_spring.common.beans.security.oauth.provider.SecurityGoogleUserInfo;
import com.ktdsuniversity.edu.hello_spring.common.beans.security.oauth.provider.SecurityNaverUserInfo;
import com.ktdsuniversity.edu.hello_spring.member.dao.MemberDao;
import com.ktdsuniversity.edu.hello_spring.member.vo.MemberVO;

/**
 * OAuth2.0 인증을 수행(요청보냄)하고 결과를 받아와 시큐리티 인증을 수행한다.
 */
@Service
public class SecurityOAuthService  implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	@Autowired
	private MemberDao memberDao;
	
	/**
	 * SpringSecurityOAuth가 OAuth Service Provider(Naver, Google, facebook)에게 인증을 요청하고 응답을 받아온다.
	 */
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		// 요청을 수행하는 객체 생성(OAuth 인증 요청) - delegate(위임)
		OAuth2UserService<OAuth2UserRequest, OAuth2User> OAuthRequest = new DefaultOAuth2UserService();
		
		// OAuth Provider(Naver, Google)에게 인증을 요청하고 사용자의 정보(scope)를 받아온다.
		OAuth2User oAuth2User= OAuthRequest.loadUser(userRequest);
		
		// Provider 인증 객체를 각각 생성한다. (scope를 가져오는 방법이 다 다르기 때문에)
		//  1. OAuth 요청을 보낼 provider의 이름 취득.
		String provider = userRequest.getClientRegistration().getRegistrationId();
		
		SecurityOAuth2UserInfo oAuthuserScope = null;
		// Naver
		if (provider.equals("naver")) {
			oAuthuserScope = new SecurityNaverUserInfo(oAuth2User.getAttributes());
		}
		// Google
		else if (provider.equals("google")) {
			oAuthuserScope = new SecurityGoogleUserInfo(oAuth2User.getAttributes());
		}
		
		// 시큐리티 인증 수행
		MemberVO oAuthMember = new MemberVO();
		oAuthMember.setEmail(oAuthuserScope.getEmailInScope());
		oAuthMember.setName(oAuthuserScope.getNameInScope());
		oAuthMember.setPassword("none");
		oAuthMember.setSalt(provider);
		oAuthMember.setRole("ROLE_USER"); // oAuth통해 가입한 사람은 무조건 유저
		oAuthMember.setProvider(provider);
		oAuthMember.setPicture(oAuthuserScope.getProfileImageInScope()); //사진 가져오고 싶으면 이렇게

		// oAuth 인증 유저 데이터베이스에 저장.
		memberDao.mergeMember(oAuthMember);
		oAuthMember = this.memberDao.selectMemberByEmail(oAuthMember.getEmail());
		
		SecurityOAuthUserDetails oAuthUserDetails = new SecurityOAuthUserDetails(oAuthMember, oAuthuserScope);
		return oAuthUserDetails;
	}
	
}

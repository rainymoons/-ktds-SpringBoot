package com.ktdsuniversity.edu.hello_spring.common.beans.security.jwt;

import java.time.Duration;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktdsuniversity.edu.hello_spring.member.vo.MemberVO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JsonWebTokenProvider {
	
	@Value("${app.jwt.issuer:hello-spring-security}")
	private String issuer;
	
	@Value("${app.jwt.secret-key:spring-security-secret-key-random-token-key}")
	private String secretKey;
	
	/**
	 * 사용자에게 보내줄 토큰을 생성한다.
	 * @param duration : 토큰의 유효 기간
	 * @param memverVO : 토큰에 넣을 사용자 정보
	 * @return JWT
	 */
	public String generateJwt(Duration duration, MemberVO memberVO) {
		
		// 1. 토큰의 유효기간 생성 (현재시간 + Duration)
		Date now = new Date();
		Date expiry = new Date(now.getTime() + duration.toMillis()); // 토큰 만료 시간
		
		
		// 2. 토큰 암/복호화를 위한 키 생성. (secretKey 이용)
		SecretKey tokenKey = Keys.hmacShaKeyFor(this.secretKey.getBytes()); // 이 키는 절대 노출되면 안됨. secretKey를 바이트 배열로 바꾸어서 넣어준다.
		
		// 3. 토큰 생성 후 반환
		
		
		
		return Jwts.builder()
				// JWT를 발급한 주체 
				.issuer(issuer)
				// JWT Token의 이름.
				.subject("SpringSecurityJwtToken")
				// JWT에 포함시킬 회원의 정보를 삽입
				.claim("user", memberVO) // memberVO를 통째로 집어넣을지, 아니면 하나하나 분리해서 넣을지는 선택사항
				.claim("email", memberVO.getEmail())
				.claim("name", memberVO.getName())
				.claim("authority", memberVO.getAuthority())
				// JWT의 발급 시간
				.issuedAt(now)
				// JWT의 유효 시간을 설정
				.expiration(expiry)
				// JWT 암호화에 사용될 비밀키를 설정.
				.signWith(tokenKey)
				// JWT를 문자열 형태로 변환시킨다.(바이트 배열인 상태이므로)
				.compact();
	}
	
	// 사용자를 보내준 jwt를 받는다.
	/**
	 * 사용자가 보내준 토큰을 검증해 사용자 정보를 조회한다.
	 * @param jwt : 사용자가 보낸 토큰
	 * @return 토큰 내부에 있는 회원의 정보
	 * @throws JsonProcessingException 
	 */
	public MemberVO getMemberFromJwt(String jwt) throws JsonProcessingException {
		
		// 토큰을 검증한다. -> 토큰을 검증한 결과는 MemberVO
		  // 암호화에 사용된 비밀키를 이용해서 복호화를 진행한다.
		SecretKey tokenKey = Keys.hmacShaKeyFor(this.secretKey.getBytes());
		
		Claims claims = Jwts.parser()
						// 암호화된 토큰을 복호화 시킨다.
						.verifyWith(tokenKey)
						// 복호화된 토큰이 이 시스템이 만든것인지 검증한다.
						.requireIssuer(this.issuer)
						// 복호화된 토큰의 제목(이름)이 발급된 토큰의 이름과 같은지 검증한다.
						.requireSubject("SpringSecurityJwtToken") // issuer와 "SpringSecurityJwtToken"가 안맞으면 변조된 토큰이다.
						// 토큰 복호화 진행. -> 진행하면 claim들을 가져올 수 있다.
						.build()
						// claim을 가져오기 위한 작업
						.parseSignedClaims(jwt)
						// getPayload()하면 claim을 준다.
						.getPayload();

		// 토큰을 검증하는 과정에서 예외가 발생했을 경우
		  // 1. 토큰의 유효기간이 만료되었을 경우
		  // 2. 토큰이 변조되었을 경우.

		// 검증이 성공했을 경우.
		  // MemberVO를 반환시킨다.
		
		// Token에서 조회된 회원의 정보  -- claim의 key를 통해서 불러온다.MemberVO타입의 클래스로 반환해줘라.
		// MemberVO memberVO = claims.get("user", MemberVO.class);
		Object jwtUser = claims.get("user");
		String email = claims.get("email", String.class);

		// JSON 데이터를 MemberVO 인스턴스로 반환한다.
		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(jwtUser);
		MemberVO memberVO = om.readValue(json, MemberVO.class);
		return memberVO;
	}
}

package com.ktdsuniversity.edu.hello_spring.common.beans.security.jwt;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ktdsuniversity.edu.hello_spring.common.beans.Sha;
import com.ktdsuniversity.edu.hello_spring.common.vo.ApiResponse;
import com.ktdsuniversity.edu.hello_spring.member.dao.MemberDao;
import com.ktdsuniversity.edu.hello_spring.member.vo.LoginMemberVO;
import com.ktdsuniversity.edu.hello_spring.member.vo.MemberVO;

@RestController
public class JsonWebTokenEntryPoint {
	
	@Autowired
	private JsonWebTokenProvider jsonWebTokenProvider;
	
	@Autowired
	private MemberDao memberDao; // 회원 조회용
	
	// memberDao로 회원을 조회해서 jsonWebTokenProvider를 이용해가지고 토큰을 만든다.
	
	@PostMapping("/token")
	public ApiResponse generateToken(@RequestBody LoginMemberVO loginMemberVO) {
		
		String email = loginMemberVO.getEmail();
		MemberVO memberVO = this.memberDao.selectMemberByEmail(email);
		// 이메일 검증(사실상 아이디)
		if (memberVO == null) {
			//return ResponseEntity.status(HttpStatus.FORBIDDEN).body("아이디 또는 비밀번호가 일치하지 않습니다.");
			ApiResponse errorResponse = new ApiResponse(HttpStatus.FORBIDDEN);
			errorResponse.setErrors(List.of("아이디 또는 비밀번호가 일치하지 않습니다."));
			return errorResponse;
		}
		
		String password = loginMemberVO.getPassword();
		Sha sha = new Sha();
		String salt = memberVO.getSalt();
		String encryptedPassword = sha.getEncrypt(password, salt);
		// 패스워드 검증
		if (!encryptedPassword.equals(memberVO.getPassword())) {
			//return ResponseEntity.status(HttpStatus.FORBIDDEN).body("아이디 또는 비밀번호가 일치하지 않습니다.");
			ApiResponse errorResponse = new ApiResponse(HttpStatus.FORBIDDEN);
			errorResponse.setErrors(List.of("아이디 또는 비밀번호가 일치하지 않습니다."));
			return errorResponse;
		}
		
		// 토큰 생성(검증 다 통과했으므로)
		String jwt = this.jsonWebTokenProvider.generateJwt(Duration.ofHours(12), memberVO);
		
		//return ResponseEntity.status(HttpStatus.OK).body(jwt);
		// status가 ok일 경우 body에 jwt를 넣어서 보내줌.
		return new ApiResponse(jwt);
	}
}

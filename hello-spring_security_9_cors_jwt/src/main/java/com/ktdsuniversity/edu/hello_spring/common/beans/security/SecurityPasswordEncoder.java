package com.ktdsuniversity.edu.hello_spring.common.beans.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.ktdsuniversity.edu.hello_spring.common.beans.Sha;

/**
 * SpringSecurity에서 비밀번호 암호화와 비교를 담당하는 클래스는 PasswordEncoder 인터페이스를 구현.
 * SecurityPasswordEncoder는 이 인터페이스를 구현함으로써, SpringSecurity에서 사용자가 입력한 비밀번호를 암호화하고 비교하는 기능을 직접 제공
 * 
 * Sha 클래스를 상속받아 SHA 알고리즘을 사용해 비밀번호를 암호화
 * setSalt 메서드를 통해 salt 값을 매번 설정하게 한 이유는 동일한 인스턴스를 사용하면서 사용자별로 다르게 설정된 salt 값을 적용하기 위함.
 */

/**
 * SecurityPasswordEncoder는 PasswordEncoder 인터페이스를 구현하여 SpringSecurity 인증 과정에서 사용자 비밀번호를 암호화하고 비교하는 역할을 수행
 * 
 * PasswordEncoder 인터페이스는 SpringSecurity에서 비밀번호 암호화와 매칭을 위한 표준 인터페이스
 * SpringSecurity 인증 절차에서 사용자가 입력한 비밀번호와 데이터베이스내에 존재하는 암호화된 비밀번호를 비교하는 역할을 수행한다.
 * -- 호출 구조
 *    AuthoriztionFilter -> AuthorizationManager -> AuthorizationProvider -> 호출
 */
public class SecurityPasswordEncoder extends Sha implements PasswordEncoder {

	
	private String salt;
	
	// 생성자가 아닌 파라미터로 받아온다.
	/**
	 * 데이터베이스에 salt 값을 할당.(외부로부터 salt값 주입받음)
	 * SecurityPasswordEncoder 객체를 사용할 때, 사용자별 salt 값을 설정할 수 있게 해줌.
	 *  -> 암호화에 필요하기 때문.
	 *  -> 같은 salt로 암호화를 해야 같은 결과가 나온다. (유저 입력값 & DB 저장된 값)
	 * @param salt : 인증을 요청한 사용자의 salt
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}

	/**
	 * 암호화된 사용자의 비밀번호와 데이터베이스의 암호화된 비밀번호와 일치하는지 확인.
	 * @param rawPassword 사용자가 인증 요청한(입력한) 원시 비밀번호. (암호화 되기 전)
	 * @param encodedPassword 데이터베이스에 암호화된 비밀번호
	 * @return 비밀번호가 일치하는가?
	 */
	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		// 사용자가 입력한 비밀번호를 암호화해준다.
		String password = this.encode(rawPassword);
		return password.equals(encodedPassword);
	}
	
	
	/**
	 * 사용자가 인증 요청한 비밀번호(rawPassword)를 암호화 한다.
	 *  -> 데이터베이스에 암호화된 비밀번호와 일치하는지 확인하기 위해서
	 *  @param rawPassword 사용자가 인증 요청한(입력한) 비밀번호
	 *  @return 사용자가 요청한 암호화된 비밀번호
	 */
	@Override
	public String encode(CharSequence rawPassword) { // CharSequence == String
		// rawPassword를 SHA 암호화를 수행 + salt값 적용. -> String 타입으로 변환하여 반환.
		return super.getEncrypt(String.valueOf(rawPassword), this.salt);
	}


}

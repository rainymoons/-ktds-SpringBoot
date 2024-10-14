package com.ktdsuniversity.edu.hello_spring.member.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktdsuniversity.edu.hello_spring.access.dao.AccessLogDao;
import com.ktdsuniversity.edu.hello_spring.access.vo.AccessLogVO;
import com.ktdsuniversity.edu.hello_spring.common.beans.Sha;
import com.ktdsuniversity.edu.hello_spring.common.utils.RequestUtil;
import com.ktdsuniversity.edu.hello_spring.member.dao.MemberDao;
import com.ktdsuniversity.edu.hello_spring.member.service.MemberService;
import com.ktdsuniversity.edu.hello_spring.member.vo.LoginMemberVO;
import com.ktdsuniversity.edu.hello_spring.member.vo.MemberVO;
import com.ktdsuniversity.edu.hello_spring.member.vo.RegistMemberVO;

@Service
public class MemberServiceImpl implements MemberService{

	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private Sha sha;
	
	@Autowired
	private AccessLogDao accessLogDao;
	
	@Override
	public boolean createNewMember(RegistMemberVO registMemberVO) {
		int emailCount = this.memberDao.selectEmailCount(registMemberVO.getEmail());
		if (emailCount > 0) {
			throw new IllegalArgumentException("해당 이메일은 사용중인 이메일입니다.");
		}
		// 1. Salt 발급
		String salt = this.sha.generateSalt();
		
		// 2. 사용자의 비밀번호를 암호화
		String password = registMemberVO.getPassword();
		password = this.sha.getEncrypt(password, salt);
		
		// 3. 암호화 된 비밀번호와 비밀번호 찾기를 위한 salt 를 DB에 저장
		registMemberVO.setPassword(password);
		registMemberVO.setSalt(salt);
		
		int insertCount = this.memberDao.insertNewMember(registMemberVO);
		return insertCount > 0;
	}
	
	@Override
	public boolean checkAvailableEmail(String email) {
		return this.memberDao.selectEmailCount(email) == 0;
	}

	@Override
	public MemberVO readMember(LoginMemberVO loginMemberVO) {
		
		boolean isIpBlock = this.accessLogDao.selectLoginFailCount( RequestUtil.getIp() ) >= 5;
		if (isIpBlock) {
			throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
		}
		
		// 1. salt 조회 (ID 조회 - 이메일)
		String salt = this.memberDao.selectSalt(loginMemberVO.getEmail());
		if (salt == null) {
			AccessLogVO accessLogVO = new AccessLogVO();
			accessLogVO.setAccessType("LOGIN");
			accessLogVO.setAccessUrl( RequestUtil.getRequest().getRequestURI() );
			accessLogVO.setAccessMethod( RequestUtil.getRequest().getMethod().toUpperCase() );
			accessLogVO.setAccessIp( RequestUtil.getIp() );
			
			// 실패에 대한 Log
			this.accessLogDao.insertNewAccessLog(accessLogVO);
			
			throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
		}
		
		// 2. 사용자가 입력한 비밀번호를 salt를 이용해 암호화
		String password = loginMemberVO.getPassword();
		password = this.sha.getEncrypt(password, salt);
		loginMemberVO.setPassword(password);
		
		// 비밀번호 입력 -> 암호화 -> 멤버에서 암호회된 password가 있는지 조회 -> 있으면? -> 로그인 성공.
		// 없으면 비밀번호가 틀림
		
		// 3. 이메일과 암호화된 비밀번호로 데이터베이스에서 회원정보 조회
		MemberVO memberVO = this.memberDao.selectOneMember(loginMemberVO);
		if (memberVO == null ) {
			// 이메일은 맞게 썼지만 비밀번호를 틀리게 적었을 경우
			// LOGIN_FAIL_COUNT를 하나 증가
			// LATEST_LOGIN_FAIL_DATE를 현재날짜로 갱신
			// LATEST_LOGIN_IP를 요청자의 IP로 갱신
			loginMemberVO.setIp(RequestUtil.getIp());
			this.memberDao.updateLoginFailState(loginMemberVO);
			throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
		}
		
		// LOGIN_FAIL_COUNT가 5 이상 && 마지막 로그인 실패시간이 한시간이 지나지 않았다면
		// 정상적인 로그인 시도여도 로그인을 실패시켜야 함
		boolean isBlockState = this.memberDao.selectLoginImpossibleCount(loginMemberVO.getEmail()) > 0;
		if (isBlockState) {
			throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
		}
		// LOGIN_FAIL_COUNT가 5 이상 && 마지막 로그인 실패시간이 한시간이 지났다면
		// 혹은 LOGIN_FAIL_COUNT가 5 미만일 경우
		// 정상적인 로그인 시도일 경우 로그인을 성공
		// 이때 LOGIN_FAIL_COUNT는 0으로 초기화
		// LATEST_LOGIN_FAIL_DATE는 NULL로 초기화
		// LATEST_LOGIN_IP를 요청자의 IP로 갱신
		// LATEST_LOGIN_SUCCESS_DATE는 현재 날짜로 갱신
		loginMemberVO.setIp(RequestUtil.getIp());
		this.memberDao.updateLoginSuccessState(loginMemberVO);
		
		AccessLogVO accessLogVO = new AccessLogVO();
		accessLogVO.setAccessType("LOGIN");
		accessLogVO.setAccessEmail(loginMemberVO.getEmail());
		accessLogVO.setAccessUrl( RequestUtil.getRequest().getRequestURI() );
		accessLogVO.setAccessMethod( RequestUtil.getRequest().getMethod().toUpperCase());
		accessLogVO.setAccessIp( RequestUtil.getIp());
		accessLogVO.setLoginSuccessYn("Y");
		// 성공에 대한 Log
		this.accessLogDao.insertNewAccessLog(accessLogVO);
		
		return memberVO;
	}
	
	@Override
	public boolean deleteMember(String email) {
		return this.memberDao.deleteOneMember(email) > 0;
	}
	
}

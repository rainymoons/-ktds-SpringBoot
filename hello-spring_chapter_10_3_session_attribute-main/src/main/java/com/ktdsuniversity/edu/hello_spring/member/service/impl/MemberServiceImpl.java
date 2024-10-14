package com.ktdsuniversity.edu.hello_spring.member.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktdsuniversity.edu.hello_spring.common.beans.Sha;
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
		
		// 1. salt 조회
		String salt = this.memberDao.selectSalt(loginMemberVO.getEmail());
		if (salt == null) {
			throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
		}
		
		// 2. 사용자가 입력한 비밀번호를 salt를 이용해 암호화
		String password = loginMemberVO.getPassword();
		password = this.sha.getEncrypt(password, salt);
		loginMemberVO.setPassword(password);
		
		// 3. 이메일과 암호화된 비밀번호로 데이터베이스에서 회원정보 조회
		MemberVO memberVO = this.memberDao.selectOneMember(loginMemberVO);
		if (memberVO == null) {
			throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
		}
		
		return memberVO;
	}
	
}

package com.ktdsuniversity.edu.hello_spring.member.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ktdsuniversity.edu.hello_spring.common.beans.Sha;
import com.ktdsuniversity.edu.hello_spring.common.exceptions.AlreadyUseException;
import com.ktdsuniversity.edu.hello_spring.member.dao.MemberDao;
import com.ktdsuniversity.edu.hello_spring.member.service.MemberService;
import com.ktdsuniversity.edu.hello_spring.member.vo.RegistMemberVO;

@Service
public class MemberServiceImpl implements MemberService{

	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private Sha sha;
	
	@Transactional
	@Override
	public boolean createNewMember(RegistMemberVO registMemberVO) {
		int emailCount = this.memberDao.selectEmailCount(registMemberVO.getEmail());
		if (emailCount > 0) {
			//throw new IllegalArgumentException("해당 이메일은 사용중인 이메일입니다.");
			throw new AlreadyUseException(registMemberVO, "해당 이메일은 사용중인 이메일입니다.");
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
	
	@Transactional
	@Override
	public boolean deleteMember(String email) {
		return this.memberDao.deleteOneMember(email) > 0;
	}
	
}

package com.ktdsuniversity.edu.hello_spring.member.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktdsuniversity.edu.hello_spring.member.dao.MemberDao;
import com.ktdsuniversity.edu.hello_spring.member.service.MemberService;
import com.ktdsuniversity.edu.hello_spring.member.vo.InsertNewMemberVO;


@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private MemberDao memberDao;

	@Override
	public boolean InsertNewMember(InsertNewMemberVO insertNewMemberVO) {
		int emailCount = memberDao.getEmailCount(insertNewMemberVO.getEmail());
		
		if(emailCount > 0) {
			throw new RuntimeException();
		}
		int insertCount = memberDao.insertNewMember(insertNewMemberVO);
			
		return insertCount > 0 ;
	}
	
	@Override
	public boolean checkAvailableEmail(String email) {
		return this.memberDao.getEmailCount(email) == 0;
	}
}

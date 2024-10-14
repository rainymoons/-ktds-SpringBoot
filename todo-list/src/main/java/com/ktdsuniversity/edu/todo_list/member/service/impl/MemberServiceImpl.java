package com.ktdsuniversity.edu.todo_list.member.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktdsuniversity.edu.todo_list.access.dao.AccessLogDao;
import com.ktdsuniversity.edu.todo_list.access.vo.AccessLogVO;
import com.ktdsuniversity.edu.todo_list.common.beans.Sha;
import com.ktdsuniversity.edu.todo_list.common.utils.RequestUtil;
import com.ktdsuniversity.edu.todo_list.member.dao.MemberDao;
import com.ktdsuniversity.edu.todo_list.member.service.MemberService;
import com.ktdsuniversity.edu.todo_list.member.vo.LoginMemberVO;
import com.ktdsuniversity.edu.todo_list.member.vo.MemberVO;
import com.ktdsuniversity.edu.todo_list.member.vo.RegistMemberVO;

@Service
public class MemberServiceImpl implements MemberService {

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
		String salt = this.sha.generateSalt();
		String password = registMemberVO.getPassword();
		
		password = this.sha.getEncrypt(password, salt);
		
		registMemberVO.setPassword(password);
		registMemberVO.setSalt(salt);
		
		return this.memberDao.insertNewMember(registMemberVO) > 0;
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
		
		String salt = this.memberDao.selectSalt(loginMemberVO.getEmail());
		if (salt == null) {
			AccessLogVO accessLogVO = new AccessLogVO();
			accessLogVO.setAccessType("LOGIN");
			accessLogVO.setAccessUrl( RequestUtil.getRequest().getRequestURI() );
			accessLogVO.setAccessMethod( RequestUtil.getRequest().getMethod() );
			accessLogVO.setAccessIp( RequestUtil.getIp() );
			
			this.accessLogDao.insertNewAccessLog(accessLogVO);
			
			throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
		}
		String password = loginMemberVO.getPassword();
		password = this.sha.getEncrypt(password, salt);
		loginMemberVO.setPassword(password);
		
		MemberVO memberVO = this.memberDao.selectOneMember(loginMemberVO);
		if (memberVO == null) {
			loginMemberVO.setIp(RequestUtil.getIp());
			this.memberDao.updateLoginFailState(loginMemberVO);
			AccessLogVO accessLogVO = new AccessLogVO();
			accessLogVO.setAccessType("LOGIN");
			accessLogVO.setAccessUrl( RequestUtil.getRequest().getRequestURI() );
			accessLogVO.setAccessMethod( RequestUtil.getRequest().getMethod() );
			accessLogVO.setAccessIp( RequestUtil.getIp() );
			
			this.accessLogDao.insertNewAccessLog(accessLogVO);
			throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
		}
		
		boolean isBlockState = this.memberDao.selectLoginImpossibleCount(loginMemberVO.getEmail()) > 0;
		if(isBlockState) {
			throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
		}
		loginMemberVO.setIp(RequestUtil.getIp());
		this.memberDao.updateLoginSuccessState(loginMemberVO);
		
		AccessLogVO accessLogVO = new AccessLogVO();
		accessLogVO.setAccessType("LOGIN");
		accessLogVO.setAccessEmail( loginMemberVO.getEmail() );
		accessLogVO.setAccessUrl( RequestUtil.getRequest().getRequestURI() );
		accessLogVO.setAccessMethod( RequestUtil.getRequest().getMethod() );
		accessLogVO.setAccessIp( RequestUtil.getIp() );
		accessLogVO.setLoginSuccessYn("Y");
		
		this.accessLogDao.insertNewAccessLog(accessLogVO);
		
		return memberVO;
	}

	@Override
	public boolean deleteMember(String email) {
		return this.memberDao.deleteOneMember(email) > 0;
	}
	
}

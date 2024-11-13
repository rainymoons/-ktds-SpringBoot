package com.ktdsuniversity.edu.hello_spring.member.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ktdsuniversity.edu.hello_spring.member.dao.MemberDao;
import com.ktdsuniversity.edu.hello_spring.member.vo.LoginMemberVO;
import com.ktdsuniversity.edu.hello_spring.member.vo.MemberVO;
import com.ktdsuniversity.edu.hello_spring.member.vo.RegistMemberVO;

@Repository
public class MemberDaoImpl extends SqlSessionDaoSupport implements MemberDao {

	@Autowired
	@Override
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}

	@Override
	public int insertNewMember(RegistMemberVO registMemberVO) {
		return this.getSqlSession().insert(NAMESPACE + ".insertNewMember", registMemberVO);
	}

	@Override
	public int selectEmailCount(String Email) {
		return this.getSqlSession().selectOne(NAMESPACE + ".selectEmailCount", Email);
	}

	@Override
	public String selectSalt(String email) {
		return this.getSqlSession().selectOne(NAMESPACE + ".selectSalt", email);
	}

	@Override
	public int updateLoginFailState(LoginMemberVO loginMemberVO) {
		return this.getSqlSession().update(NAMESPACE + ".updateLoginFailState", loginMemberVO);
	}

	@Override
	public int selectLoginImpossibleCount(String email) {
		return this.getSqlSession().selectOne(NAMESPACE + ".selectLoginImpossibleCount", email);
	}
	
	@Override
	public int updateLoginSuccessState(LoginMemberVO loginMemberVO) {
		return this.getSqlSession().update(NAMESPACE + ".updateLoginSuccessState", loginMemberVO);
	}
	
	@Override
	public int deleteOneMember(String email) {
		return this.getSqlSession().delete(NAMESPACE + ".deleteOneMember", email);
	}

	@Override
	public MemberVO selectMemberByEmail(String username) {
		return this.getSqlSession().selectOne(NAMESPACE + ".selectMemberByEmail", username);
	}
}

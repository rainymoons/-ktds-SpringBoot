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
	public int selectEmailCount(String email) {
		return getSqlSession().selectOne( NAMESPACE + ".selectEmailCount", email);
	}

	@Override
	public int insertNewMember(RegistMemberVO registMemberVO) {
		return getSqlSession().insert( NAMESPACE + ".insertNewMember", registMemberVO);
	}

	@Override
	public String selectSalt(String email) {
		return getSqlSession().selectOne( NAMESPACE + ".selectSalt", email);
	}

	@Override
	public MemberVO selectOneMember(LoginMemberVO loginMemberVO) {
		return getSqlSession().selectOne( NAMESPACE + ".selectOneMember", loginMemberVO);
	}
	
}

package com.ktdsuniversity.edu.hello_spring.member.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;

import com.ktdsuniversity.edu.hello_spring.member.dao.MemberDao;
import com.ktdsuniversity.edu.hello_spring.member.vo.InsertNewMemberVO;

public class MemberDaoImpl extends SqlSessionDaoSupport implements MemberDao {
	
	@Autowired
	@Override
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}
	
	@Override
	public int getEmailCount(String email) {
		return getSqlSession().selectOne("", email);
	}

	@Override
	public int insertNewMember(InsertNewMemberVO insertNewMemberVO) {
		return getSqlSession().insert("", insertNewMemberVO);
	}
}

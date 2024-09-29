package com.ktdsuniversity.edu.hello_spring.bbs.dao.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ktdsuniversity.edu.hello_spring.bbs.dao.BoardDao;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardVO;

@Repository
public class BoardDaoImpl extends SqlSessionDaoSupport implements BoardDao {
	
	@Autowired
	@Override
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		// Autowired 됬는지 확인
		System.out.println("Autowiring sqlSessionTemplate: " + sqlSessionTemplate);
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}
	
	@Override
	public int selectBoardAllCount() {
		// getSqlSession을 통해서 한개의 ROW만 조회를 하겠다. 앞에 namespace를 적어줘서 안전하게 호출
		return this.getSqlSession()
				.selectOne("com.ktdsuniversity.edu.hello_spring.bbs.BoardDao.selectBoardAllCount");
	}
	
	@Override
	public List<BoardVO> selectAllBoard() {
		return this.getSqlSession()
				.selectList("com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardVO.selectAllBoard");
	}

}

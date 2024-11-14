package com.ktdsuniversity.edu.hello_spring.bbs.dao.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ktdsuniversity.edu.hello_spring.bbs.dao.BoardDao;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.DeleteBoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ModifyBoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.SearchBoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteBoardVO;

@Repository
public class BoardDaoImpl extends SqlSessionDaoSupport implements BoardDao {

	public static final Logger logger = LoggerFactory.getLogger(BoardDaoImpl.class);
	
	@Autowired
	@Override
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		// Debug 레벨의 로거를 사용할 수 있을까?
		if (logger.isDebugEnabled()) {
			logger.debug("Autowiring sqlSessionTemplate: " + sqlSessionTemplate);
		}
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}
	
	@Override
	public int selectBoardAllCount(SearchBoardVO searchBoardVO) {
		// 1개만 가져오기를 원함(1개의 로우만 원함 2개 이상 나오면 ERROR) -> selectOne()
		return this.getSqlSession().selectOne("com.ktdsuniversity.edu.hello_spring.bbs.dao.BoardDao.selectBoardAllCount", searchBoardVO);
	}
	
	@Override
	public List<BoardVO> selectAllBoard() {
		return this.getSqlSession().selectList("com.ktdsuniversity.edu.hello_spring.bbs.dao.BoardDao.selectAllBoard");
	}
	
	@Override
	public List<BoardVO> selectAllBoard(SearchBoardVO searchBoardVO) {
		return this.getSqlSession().selectList("com.ktdsuniversity.edu.hello_spring.bbs.dao.BoardDao.selectAllBoard", searchBoardVO);
	}
	
	@Override
	public int insertNewBoard(WriteBoardVO writeBoardVO) {
		// 파라미터는 1개만 보낼 수 있음
		return this.getSqlSession().insert("com.ktdsuniversity.edu.hello_spring.bbs.dao.BoardDao.insertNewBoard", writeBoardVO);
	}
	
	@Override
	public int updateViewCount(int id) {
		return this.getSqlSession().update("com.ktdsuniversity.edu.hello_spring.bbs.dao.BoardDao.updateViewCount", id);
	}

	@Override
	public BoardVO selectOneBoard(int id) {
		return this.getSqlSession().selectOne("com.ktdsuniversity.edu.hello_spring.bbs.dao.BoardDao.selectOneBoard", id);
	}
	
	@Override
	public int updateOneBoard(ModifyBoardVO modifyBoardVO) {
		return this.getSqlSession().update("com.ktdsuniversity.edu.hello_spring.bbs.dao.BoardDao.updateOneBoard", modifyBoardVO);
	}
	
	@Override
	public int deleteOneBoard(DeleteBoardVO deleteBoardVO) {
		return this.getSqlSession().delete("com.ktdsuniversity.edu.hello_spring.bbs.dao.BoardDao.deleteOneBoard", deleteBoardVO);
	}
	
}
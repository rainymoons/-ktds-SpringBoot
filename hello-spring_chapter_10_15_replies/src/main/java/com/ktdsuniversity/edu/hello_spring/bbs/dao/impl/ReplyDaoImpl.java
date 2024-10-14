package com.ktdsuniversity.edu.hello_spring.bbs.dao.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ktdsuniversity.edu.hello_spring.bbs.dao.ReplyDao;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ModifyReplyVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ReplyVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteReplyVO;

@Repository
public class ReplyDaoImpl extends SqlSessionDaoSupport implements ReplyDao{

	@Autowired
	@Override
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}
	
	@Override
	public List<ReplyVO> getAllReplies(int boardId) {
		return getSqlSession().selectList("com.ktdsuniversity.edu.hello_spring.bbs.dao.ReplyDao.getAllReplies", boardId);
	}
	
	@Override
	public ReplyVO getOneReply(int replyId) {
		return this.getSqlSession().selectOne("com.ktdsuniversity.edu.hello_spring.bbs.dao.ReplyDao.getOneReply", replyId);
	}
	
	@Override
	public int createNewReply(WriteReplyVO writeReplyVO) {
		return this.getSqlSession().insert("com.ktdsuniversity.edu.hello_spring.bbs.dao.ReplyDao.createNewReply", writeReplyVO);
	}
	
	@Override
	public int deleteOneReply(int replyId) {
		return this.getSqlSession().delete("com.ktdsuniversity.edu.hello_spring.bbs.dao.ReplyDao.deleteOneReply", replyId);
	}
	
	@Override
	public int updateOneReply(ModifyReplyVO modifyReplyVO) {
		return this.getSqlSession().update("com.ktdsuniversity.edu.hello_spring.bbs.dao.ReplyDao.updateOneReply", modifyReplyVO);
	}
	
	@Override
	public int updateRecommendCountOneReply(int replyId) {
		return this.getSqlSession().update("com.ktdsuniversity.edu.hello_spring.bbs.dao.ReplyDao.updateRecommendCountOneReply", replyId);
	}
}

package com.ktdsuniversity.edu.hello_spring.bbs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktdsuniversity.edu.hello_spring.bbs.dao.ReplyDao;
import com.ktdsuniversity.edu.hello_spring.bbs.service.ReplyService;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ModifyReplyVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ReplyVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteReplyVO;
import com.ktdsuniversity.edu.hello_spring.common.exceptions.PageNotFoundException;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private ReplyDao replyDao;
	
	@Override
	public List<ReplyVO> getAllReplies(int boardId) {
		return replyDao.getAllReplies(boardId);
	}
	
	@Override
	public boolean createNewReply(WriteReplyVO writeReplyVO) {
		int count = replyDao.createNewReply(writeReplyVO);
		return count > 0;
	}
	
	@Override
	public boolean deleteOneReply(int replyId, String email) {
		ReplyVO replyVO = replyDao.getOneReply(replyId);
		if (!email.equals(replyVO.getEmail())) {
			throw new PageNotFoundException("잘못된 접근입니다.");
		}
		return replyDao.deleteOneReply(replyId) > 0;
	}
	
	@Override
	public boolean updateOneReply(ModifyReplyVO modifyReplyVO) {
		ReplyVO oldReplyVO = replyDao.getOneReply(modifyReplyVO.getReplyId());
		if (!modifyReplyVO.getEmail().equals(oldReplyVO.getEmail())) {
			throw new PageNotFoundException("잘못된 접근입니다.");
		}
		return replyDao.updateOneReply(modifyReplyVO) > 0;
	}
	
	@Override
	public boolean recommendOneReply(int replyId, String email) {
		//int count = replyDao.updateRecommendCountOneReply(replyId);
		ReplyVO replyVO = replyDao.getOneReply(replyId);
		if (email.equals(replyVO.getEmail())) {
			throw new PageNotFoundException("잘못된 접근입니다.");
		}
		return replyDao.updateRecommendCountOneReply(replyId) > 0;
	}
}

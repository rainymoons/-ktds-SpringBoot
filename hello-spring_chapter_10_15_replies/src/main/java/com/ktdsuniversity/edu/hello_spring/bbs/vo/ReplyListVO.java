package com.ktdsuniversity.edu.hello_spring.bbs.vo;

import java.util.List;

public class ReplyListVO {

	List<ReplyVO> replyList;
	
	private int recommendCnt;

	public List<ReplyVO> getReplyList() {
		return replyList;
	}

	public void setReplyList(List<ReplyVO> replyList) {
		this.replyList = replyList;
	}

	public int getRecommendCnt() {
		return recommendCnt;
	}

	public void setRecommendCnt(int recommendCnt) {
		this.recommendCnt = recommendCnt;
	}
}

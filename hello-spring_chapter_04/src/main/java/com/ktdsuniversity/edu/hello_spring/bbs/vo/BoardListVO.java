package com.ktdsuniversity.edu.hello_spring.bbs.vo;

import java.util.List;

public class BoardListVO {

	/**
	 * 조회된 게시글의 수
	 * 조회된 게시글이 boardList에 저장되더라도 매번 boardList.size()를 수행하는 것은 성능상 좋지 않음.
	 * 페이지네이션에서도 사용된다.
	 */
	private int boardCnt;
	
	/**
	 * 조회된 게시글의 목록
	 */
	private List<BoardVO> boardList;

	public int getBoardCnt() {
		return boardCnt;
	}

	public void setBoardCnt(int boardCnt) {
		this.boardCnt = boardCnt;
	}

	public List<BoardVO> getBoardList() {
		return boardList;
	}

	public void setBoardList(List<BoardVO> boardList) {
		this.boardList = boardList;
	}
}

package com.ktdsuniversity.edu.hello_spring.bbs.service;

import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardListVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ModifyBoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteBoardVO;

public interface BoardService {

	public BoardListVO getAllBoard();
	
	// create 성공, 실패 여부 반환
	public boolean createNewBoard(WriteBoardVO writeBoardVO);
	
	/**
	 * 파라미터로 전달받은 id로 게시글을 조회한다.
	 * 게시글 조회시 조회수도 1 증가한다.
	 * @param id 조회할 게시글의 id
	 * @param isIncrease 값이 true일 경우 조회수를 증가시킨다. false일 경우 증가시키지 않음.
	 * @return 게시글 정보
	 */
	public BoardVO getOneBoard(int id, boolean isIncrease);
	
	public boolean updateOneBoard(ModifyBoardVO modifyBoardVO);
	
	public boolean deleteOneBoard(int id);
}
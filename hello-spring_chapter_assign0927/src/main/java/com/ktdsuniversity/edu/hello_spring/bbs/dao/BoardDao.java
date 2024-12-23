package com.ktdsuniversity.edu.hello_spring.bbs.dao;

import java.util.List;

import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteBoardVO;

public interface BoardDao {
	
	
	public int selectBoardAllCount();
	
	public List<BoardVO> selectAllBoard();
	
	// insert한 갯수 반환.
	public int insertNewBoard(WriteBoardVO writeBoardVO);
	
	/**
	 * 파라미터로 전달받은 게시글 ID의 조회수를 1 증가시킨다.
	 * @param id 게시글 ID(번호)
	 * @return DB에 업데이트한 개수
	 */
	public int increaseViewCount(int id);
	
	/**
	 * 파라미터로 전달받은 게시글 ID의 게시글 정보를 조회한다.
	 * @param id 게시글 ID(번호)
	 * @return 
	 */
	public BoardVO selectOneBoard(int id);
	
}

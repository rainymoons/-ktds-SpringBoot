package com.ktdsuniversity.edu.hello_spring.bbs.dao;

import java.util.List;

import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ModifyBoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteBoardVO;

public interface BoardDao {

	public int selectBoardAllCount();
	
	public List<BoardVO> selectAllBoard();
	
	public int insertNewBoard(WriteBoardVO writeBoardVO);
	
	/**
	 * 파라미터로 받아온 게시글의 조회수를 증가
	 * @param id 게시글 ID
	 * @return
	 */
	public int updateViewCount(int id);
	
	/**
	 * 파라미터로 받아온 게시글을 가져옴
	 * @param id 게시글 ID
	 * @return
	 */
	public BoardVO selectOneBoard(int id);
	
	public int updateOneBoard(ModifyBoardVO modifyBoardVO);
	
	/**
	 * 파라미터로 받아온 ID의 게시글을 삭제함
	 * @param id 게시글 ID
	 * @return
	 */
	public int deleteOneBoard(int id);
	
}

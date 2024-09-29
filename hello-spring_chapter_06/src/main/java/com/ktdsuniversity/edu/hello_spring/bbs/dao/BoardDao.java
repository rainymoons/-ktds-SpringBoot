package com.ktdsuniversity.edu.hello_spring.bbs.dao;

import java.util.List;

import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteBoardVO;

public interface BoardDao {
	
	
	public int selectBoardAllCount();
	
	public List<BoardVO> selectAllBoard();
	
	// insert한 갯수 반환.
	public int insertNewBoard(WriteBoardVO writeBoardVO);
	
}

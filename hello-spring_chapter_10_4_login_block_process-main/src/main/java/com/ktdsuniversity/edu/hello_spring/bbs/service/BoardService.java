package com.ktdsuniversity.edu.hello_spring.bbs.service;

import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardListVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ModifyBoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteBoardVO;

public interface BoardService {

	public BoardListVO getAllBoard();
	
	public boolean creatNewBoard(WriteBoardVO writeBoardVO);
	
	public BoardVO getOneBoard(int id, boolean isIncrease);

	public boolean updateOneBoard(ModifyBoardVO modifyBoardVO);
	
	public boolean deleteOneBoard(int id);
	
}

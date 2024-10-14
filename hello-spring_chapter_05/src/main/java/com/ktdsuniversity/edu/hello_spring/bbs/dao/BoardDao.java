package com.ktdsuniversity.edu.hello_spring.bbs.dao;

import java.util.List;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardVO;

public interface BoardDao {
	
	public int selectBoardAllCount();
	
	public List<BoardVO> selectAllBoard();
}

package com.ktdsuniversity.edu.hello_spring.bbs.dao;

import java.util.List;

import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.DeleteBoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ModifyBoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.SearchBoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteBoardVO;

public interface BoardDao {

	public int selectBoardAllCount(SearchBoardVO searchBoardVO);
	
	/**
	 * 페이지네이션 처리 없이 모든 게시글을 조회한다.
	 * 엑셀 다운로드에서 사용하게 된다.
	 * @return
	 */
	public List<BoardVO> selectAllBoard();
	
	/**
	 * 페이지네이션 처리한 게시글을 조회한다.
	 * 목록 페이지에서 사용한다.
	 * @param searchBoardVO
	 * @return
	 */
	public List<BoardVO> selectAllBoard(SearchBoardVO searchBoardVO);
	
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
	public int deleteOneBoard(DeleteBoardVO deleteBoardVO);
	
}

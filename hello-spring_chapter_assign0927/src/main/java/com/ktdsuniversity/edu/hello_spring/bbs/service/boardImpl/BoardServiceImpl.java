package com.ktdsuniversity.edu.hello_spring.bbs.service.boardImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktdsuniversity.edu.hello_spring.bbs.dao.BoardDao;
import com.ktdsuniversity.edu.hello_spring.bbs.service.BoardService;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardListVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteBoardVO;

@Service
public class BoardServiceImpl implements BoardService {
	
	// Service의 역할. DAO 호출. -> BoardDaoImpl을 Autowired
	@Autowired
	private BoardDao boardDao;
	
	@Override
	public BoardListVO getAllBoard() {
		// 게시글 목록 화면에 데이터를 전송해주기 위해서 게시글의 건수와 게시글의 목록을 조회해 반환시킨다.
		
		// 1. 게시글의 건수 조회
		int boardCount = this.boardDao.selectBoardAllCount();
		
		// 2. 게시글의 목록 조회
		List<BoardVO> boardList = this.boardDao.selectAllBoard();
		
		// 3. BoardListVO를 만들어서 게시글의 건수와 목록을 할당한다.
		BoardListVO boardListVO = new BoardListVO();
		boardListVO.setBoardCnt(boardCount);
		boardListVO.setBoardList(boardList);
		
		// 4. BoardListVO 인스턴스를 반환한다.
		return boardListVO;
	}
	
	@Override
	public boolean createNewBoard(WriteBoardVO writeBoardVO) {
		int result = this.boardDao.insertNewBoard(writeBoardVO); 
		return result == 1;
	}
	
	@Override
	public BoardVO getOneBoard(int id) {
		// 파라미터로 전달 받은 게시글의 조회 수 증가
		// updateCount에는 DB에 업데이트한 게시글의 수를 반환
		int updateCount = boardDao.increaseViewCount(id);
		if (updateCount == 0) {
			// UpdateCount가 0 이라는 것은 파라미터로 전달받은 id 값이 DB에 존재하지 않는다는 것
			// 이 경우 잘못된 접근입니다. 라고 사용자에게 예외 메시지를 전달함
			throw new IllegalArgumentException("잘못된 접근입니다.");
		}
		// 예외가 발생하지 않으면 게시글 정보를 조회한다.
		BoardVO boardVO = boardDao.selectOneBoard(id);
		return boardVO;
	}
}

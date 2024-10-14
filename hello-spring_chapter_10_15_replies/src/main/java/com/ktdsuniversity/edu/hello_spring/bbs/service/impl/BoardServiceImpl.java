package com.ktdsuniversity.edu.hello_spring.bbs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ktdsuniversity.edu.hello_spring.bbs.dao.BoardDao;
import com.ktdsuniversity.edu.hello_spring.bbs.service.BoardService;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardListVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.DeleteBoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ModifyBoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteBoardVO;
import com.ktdsuniversity.edu.hello_spring.common.beans.FileHandler;
import com.ktdsuniversity.edu.hello_spring.common.exceptions.PageNotFoundException;
import com.ktdsuniversity.edu.hello_spring.common.vo.StoreResultVO;

@Service
public class BoardServiceImpl implements BoardService {

	
//	@Value("${app.multipart.base-dir}")
//	private String baseDirectory;
	
	@Autowired
	private FileHandler fileHandler;
	
	@Autowired
	private BoardDao boardDao;
	
	@Override
	public BoardListVO getAllBoard() {
		// 게시글 목록 화면에 데이터를 전송해주기 위해서 
		// 게시글의 건수와 게시글의 목록을 조회해 반환시킨다.
		
		// 1. 게시글의 건수를 조회
		int boardCount = this.boardDao.selectBoardAllCount();
		
		// 2. 게시글의 목록을 조회
		List<BoardVO> boardList = this.boardDao.selectAllBoard();
		
		// 3. BoardListVO를 만들어서 게시글의 건수와 목록을 할당한다
		BoardListVO boardListVO = new BoardListVO();
		boardListVO.setBoardCnt(boardCount);
		boardListVO.setBoardList(boardList);
		
		// 4. BoardListVO인스턴스를 반환
		return boardListVO;
	}
	
	@Transactional // 서비스 코드가 동작하는 중에 예외가 발생하면, Rollback 해준다. 예외가 발생하지 않으면 Commit 해준다.
	@Override
	public boolean creatNewBoard(WriteBoardVO writeBoardVO) {
		
		// 파일 업로드 처리
		MultipartFile file = writeBoardVO.getFile();
		
		StoreResultVO storeResultVO = this.fileHandler.storeFile(file); // 업로드 했으면 인스턴스가 있고 안했으면 null
		if(storeResultVO != null) {
			writeBoardVO.setFileName( storeResultVO.getObfuscatedFileName() );
			writeBoardVO.setOriginFileName( storeResultVO.getOriginFileName() );
		}
		
		// DB에 등록한 게시글의 개수
		int createCount = boardDao.insertNewBoard(writeBoardVO);
		
		// 강제로 예외 발생시킴 : 트랜잭션 적용 여부를 알기 위해
		// @Transactional이 적용되어 있지 않았을 때, Rollback이 되지 않아야 한다. 를 검증.
//		Integer.parseInt("예외발생시키기위한 트랜잭션용 덫");
		
		// 0보다 크면 true 작다면 false
		return createCount > 0;
	}
	
	@Transactional
	@Override
	public BoardVO getOneBoard(int id, boolean isIncrease) {
		if (isIncrease == true) {
			int updateViewCount = this.boardDao.updateViewCount(id);
			if (updateViewCount == 0) {
				// throw new IllegalArgumentException("잘못된 접근입니다.");
				throw new PageNotFoundException("잘못된 접근입니다.");
			}
		}
		BoardVO boardVO = this.boardDao.selectOneBoard(id);
		if (boardVO == null) {
			//throw new IllegalArgumentException("잘못된 접근입니다.");
			throw new PageNotFoundException("잘못된 접근입니다.");
		}
		return boardVO;
	}
	
	@Transactional
	@Override
	public boolean updateOneBoard(ModifyBoardVO modifyBoardVO) {
		// 기존의 파일을 삭제하기 위해 업데이트 하기 전의 게시글의 정보를 조회
		BoardVO boardVO = this.boardDao.selectOneBoard(modifyBoardVO.getId());
		
		MultipartFile file = modifyBoardVO.getFile();
		
		StoreResultVO storeResultVO = this.fileHandler.storeFile(file);
		if(storeResultVO != null) {
			modifyBoardVO.setFileName(storeResultVO.getObfuscatedFileName());
			modifyBoardVO.setOriginFileName(storeResultVO.getOriginFileName());
		}
		
		int updateCount = this.boardDao.updateOneBoard(modifyBoardVO);
		// 작성자의 이메일이 다르면 0을 반환해서 넘어옴.-> 0이면 업데이트가 되지 않음. 
		if(updateCount > 0) {
			// update 하기 전에 조회해서 원래 파일 이름을 가져와야 함
			this.fileHandler.deleteFile(boardVO.getFileName());
		}
		return updateCount > 0;
	}
	
	@Transactional
	@Override
	public boolean deleteOneBoard(DeleteBoardVO deleteBoardVO) {
		BoardVO boardVO = this.boardDao.selectOneBoard(deleteBoardVO.getId());
		
		int deleteCount = this.boardDao.deleteOneBoard(deleteBoardVO);
		
		// 사용자 인증이 되지 않으면(반환값이0이면) 삭제가 되지 않는다.
		if(deleteCount > 0) {
			this.fileHandler.deleteFile(boardVO.getFileName());
		}
		
		return deleteCount > 0;
	}
	
}

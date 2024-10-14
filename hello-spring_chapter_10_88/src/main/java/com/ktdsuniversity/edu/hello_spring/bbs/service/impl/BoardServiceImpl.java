package com.ktdsuniversity.edu.hello_spring.bbs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ktdsuniversity.edu.hello_spring.bbs.dao.BoardDao;
import com.ktdsuniversity.edu.hello_spring.bbs.service.BoardService;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardListVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ModifyBoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteBoardVO;
import com.ktdsuniversity.edu.hello_spring.common.beans.FileHandler;
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
		// 0보다 크면 true 작다면 false
		return createCount > 0;
	}
	
	@Override
	public BoardVO getOneBoard(int id, boolean isIncrease) {
		if (isIncrease == true) {
			int updateViewCount = this.boardDao.updateViewCount(id);
			if (updateViewCount == 0) {
				throw new IllegalArgumentException("잘못된 접근입니다.");
			}
		}
		BoardVO boardVO = this.boardDao.selectOneBoard(id);
		if (boardVO == null) {
			throw new IllegalArgumentException("잘못된 접근입니다.");
		}
		return boardVO;
	}
	
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
		
		if(updateCount > 0) {
			// update 하기 전에 조회해서 원래 파일 이름을 가져와야 함
			this.fileHandler.deleteFile(boardVO.getFileName());
		}
		
		return updateCount > 0;
	}
	
	@Override
	public boolean deleteOneBoard(int id) {
		BoardVO boardVO = this.boardDao.selectOneBoard(id);
		
		int deleteCount = this.boardDao.deleteOneBoard(id);
		
		if(deleteCount > 0) {
			this.fileHandler.deleteFile(boardVO.getFileName());
		}
		
		return deleteCount > 0;
	}
	
}

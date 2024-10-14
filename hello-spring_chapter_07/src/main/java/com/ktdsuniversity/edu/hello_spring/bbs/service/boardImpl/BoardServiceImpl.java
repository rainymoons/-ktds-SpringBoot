package com.ktdsuniversity.edu.hello_spring.bbs.service.boardImpl;

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
	
	// application.yml파일에서 app.multipart.base-dir 설정 값을 가져온다.(가져오고자 하는 프로퍼티의 이름)
	// STEL 문법을 써야한다. ${ }
	// @Value는 Spring Bean에서만 사용이 가능하다.(클래스에 @Service가 있으므로 사용이 가능하다.)
	// Spring Bean : @Controller, @Service, @Repositroy 이 세 가지가 정의 된 클래스에서만 쓸 수 있다.(부모인 @Component를 상속한다.)
//	@Value("${app.multipart.base-dir}")
//	private String baseDirectory;

	// FileHandler가 스프링 빈에 등록되었으므로 Autowired 해온다.
	@Autowired
	private FileHandler fileHandler; // 업로드를 하기 위한 fileHandler
	
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
		
		// 파일 업로드 처리
		MultipartFile file = writeBoardVO.getFile();
		// MultiparFile 객체를 받아 fileHandler.storeFile 메서드를 통해 파일을 저장한다.
		// 저장된 파일이 있으면 storeResultVO에 결과가 저장된다.
		StoreResultVO storeResultVO = this.fileHandler.storeFile(file); // 업로드를 했으면 파일이 있고 없으면 Null
		if (storeResultVO != null) {
			// storeResultVO가 null이 아니면(즉, 파일이 업로드되었으면) 난독화된 파일 이름과 원본 파일 이름을 writeBoardVO에 설정
			writeBoardVO.setFileName(storeResultVO.getObfuscatedFileName());
			writeBoardVO.setOriginFileName(storeResultVO.getOriginFileName());
		}
		// DB에 게시글 저장
		int result = this.boardDao.insertNewBoard(writeBoardVO);
		// 삽입 성공 여부 반환
		return result == 1;
	}
	
	@Override
	public BoardVO getOneBoard(int id, boolean isIncrease) {

		if(isIncrease) {
			// 파라미터로 전달 받은 게시글의 조회 수 증가
			// updateCount에는 DB에 업데이트한 게시글의 수를 반환
			int updateCount = boardDao.increaseViewCount(id);
			if (updateCount == 0) {
				// UpdateCount가 0 이라는 것은 파라미터로 전달받은 id 값이 DB에 존재하지 않는다는 것
				// 이 경우 잘못된 접근입니다. 라고 사용자에게 예외 메시지를 전달함
				throw new IllegalArgumentException("잘못된 접근입니다.");
			}
		}
		
		// 예외가 발생하지 않으면 게시글 정보를 조회한다.
		BoardVO boardVO = boardDao.selectOneBoard(id);
		
		// isIncrease가 false일 경우 실행되어야 할 에러처리(새롭게 만들어준다.)
		// 예외가 발생하지 않았다면, 게시글 정보를 조회한다.
		if (boardVO == null) {
			
			// 파라미터로 전달 받은 id값이 DB에 존재하지 않을 경우
			// 잘못된 접근입니다. 라고 사용자에게 예외 메세지를 보내준다.
			throw new IllegalArgumentException("잘못된 접근입니다.");
		}
		
		return boardVO;
	}
	
	@Override
	public boolean updateOneBoard(ModifyBoardVO modifyBoardVO) {
		
		// 기존의 파일을 삭제하기 위해서 업데이트 하기 전 해당 게시글의 정보를 조회한다.
		BoardVO boardVO = boardDao.selectOneBoard(modifyBoardVO.getId());
		
		// 파라미터로 전달받은 수정된 게시글의 정보로 DB 수정
		// updateCount에는 DB에 업데이트한 게시글의 수를 반환.
		MultipartFile file = modifyBoardVO.getFile();
		
		StoreResultVO storeResultVO = this.fileHandler.storeFile(file);
		if (storeResultVO != null) {
			modifyBoardVO.setFileName(storeResultVO.getObfuscatedFileName());
			modifyBoardVO.setOriginFileName(storeResultVO.getOriginFileName());
		}
		// 게시글을 수정하고 수정 수행 상태를 나타내는 updateCount 변수 선언 후 값을 담는다.
		int updateCount = boardDao.updateOneBoard(modifyBoardVO);
		
		if (updateCount > 0 ) {
			//this.fileHandler.deleteFile("지워야하는 파일의 이름");
			// 얘의 정보를 가져와야 한다. 업로드 하기 전의 정보 (DB) -> update하기 전에 DB에서 셀렉트

			// 기존 파일 정보로 기존 파일 삭제
			this.fileHandler.deleteFile(boardVO.getFileName()); // 기존 파일 삭제

			// 게시글 수정 후, 수정된 게시글 정보를 다시 조회할 필요가 있다면 여기서 조회
			boardVO = boardDao.selectOneBoard(modifyBoardVO.getId()); // 최신 데이터 조회
		}
		return updateCount > 0;
	}
	
	@Override
	public boolean deleteOneBoard(int id) {
		// 기존의 파일을 삭제하기 위해서 업데이트 하기 전 해당 게시글의 정보를 조회한다.
		BoardVO boardVO = boardDao.selectOneBoard(id);
		
		int deleteCount = this.boardDao.deleteOneBoard(id);
		if (deleteCount > 0) {
			// boardVO.getFileName() -> 업데이트 하기 전 파일명
			this.fileHandler.deleteFile(boardVO.getFileName());
		}
		return deleteCount > 0;
	}
}

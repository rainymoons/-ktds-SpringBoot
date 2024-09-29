package com.ktdsuniversity.edu.hello_spring.bbs.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import com.ktdsuniversity.edu.hello_spring.bbs.dao.BoardDao;
import com.ktdsuniversity.edu.hello_spring.bbs.dao.impl.BoardDaoImpl;
import com.ktdsuniversity.edu.hello_spring.bbs.service.BoardService;
import com.ktdsuniversity.edu.hello_spring.bbs.service.BoardImpl.BoardServiceImpl;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardListVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardVO;

// MyBatis 테스트가 아니므로 SpringBootTest (Service만 테스트)
// Spring Application의 Bean을 생성해주는 Annotation. Service의 여러 DI들을 가져온다.
@SpringBootTest

// JUnit5를 사용하기 위한 설정. -> 버전이 올라가면서 Default로 JUnit5를 사용할 수 있게 된 듯.
// @ExtendWith(SpringExtension.class)

// Test하고자 하는 클래스와 Test에 필요한 클래스들을 Import한다. 
// BoardServiceImpl.class : BoardServiceImpl을 테스트하기 위해 Import
// BoardDaoImpl.class : BoardServiceImpl에 BoardDaoImpl을 Autowired하기 위해 Import. -> 데이터를 변형하기 위한 것.
@Import( {BoardServiceImpl.class, BoardDaoImpl.class} ) // -> 여러개를 가져올때는 ,가 아니라 { } 배열 기호 사용.
public class BoardServiceImplTest {

	// 1. BoardServiceImpl.class의 인스턴스를 boardService에 넣는다.
	@Autowired
	private BoardService boardService;
	
	/**
	 *JUnit5 테스트를 위해 BoardServiceImpl에 가짜 인스턴스를 DI시킨다.
	 */
	
	// 2. BoardDaoImpl.class의 인스턴스를 boardService에 넣는다. (정확히는 BoardDao) 이때는 @Autowired가 아님.
	@MockBean // 가짜 인스턴스 생성. 보드서비스임플의 boardDao를 넣으면(Autowired한) raw 데이터가 훼손되므로 실제 DB에 연결시키지 않음.
	private BoardDao boardDao;
	
	@Test //DAO는 테스트 안함.
	public void testGetAllBoard() {
		// given - when - then 패턴.
		// 검사하고 싶은 것. BoardServiceImpl의 getAllBoard()가 잘 동작하냐?
		
		// given
		// 1. boardDao.selectBoardAllCount()가 반환시킬 값을 명시한다. -> int boardCount = 3으로 설정.
		BDDMockito.given(boardDao.selectBoardAllCount()).willReturn(3);
		
		// 2. boardDao.selectAllBoard()가 반환시킬 값을 명시한다.
		//  -> List 3개 생성 했으니
		List<BoardVO> mockList = new ArrayList<>();
		mockList.add(new BoardVO());
		mockList.add(new BoardVO());
		mockList.add(new BoardVO());

		BDDMockito.given(boardDao.selectAllBoard()).willReturn(mockList);
		
		// when - 실제 테스트하려는 메서드 실행
		// 3. BoardServiceImpl 의 getAllBoard()를 호출한다.
		BoardListVO boardListVO = boardService.getAllBoard();
		
		// then - 예상 데이터와 실제 데이터 검증
		// 4. given 데이터와 실행데이터(boardListVO)가 일치하는지 검사한다.
		assertEquals(3, boardListVO.getBoardCnt()); // boardCount는 boardListVO에 들어가 있을 것이기 때문.
		assertEquals(3, boardListVO.getBoardList().size());
	}
}

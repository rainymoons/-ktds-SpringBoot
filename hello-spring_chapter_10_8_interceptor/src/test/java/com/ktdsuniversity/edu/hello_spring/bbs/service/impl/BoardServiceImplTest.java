package com.ktdsuniversity.edu.hello_spring.bbs.service.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import com.ktdsuniversity.edu.hello_spring.bbs.dao.BoardDao;
import com.ktdsuniversity.edu.hello_spring.bbs.dao.impl.BoardDaoImpl;
import com.ktdsuniversity.edu.hello_spring.bbs.service.BoardService;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteBoardVO;

@SpringBootTest
@Import({BoardServiceImpl.class, BoardDaoImpl.class})
public class BoardServiceImplTest {

	@Autowired
	private BoardService boardService;
	
	@MockBean
	private BoardDao boardDao;
	
	@Test
	public void successCreatBoard() {
		WriteBoardVO writeBoardVO = new WriteBoardVO();
		// given
		BDDMockito.given(boardDao.insertNewBoard(writeBoardVO)).willReturn(1);
		// when
		boolean isSuccess = boardService.creatNewBoard(writeBoardVO);
		// then
		assertTrue(isSuccess);
	}
	
}

package com.ktdsuniversity.edu.hello_spring.bbs.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import com.ktdsuniversity.edu.hello_spring.bbs.dao.BoardDao;
import com.ktdsuniversity.edu.hello_spring.bbs.dao.impl.BoardDaoImpl;
import com.ktdsuniversity.edu.hello_spring.bbs.service.boardImpl.BoardServiceImpl;


@Import({BoardServiceImpl.class, BoardDaoImpl.class})
@SpringBootTest
public class BoardServiceImplTest {
	
	@Autowired
	BoardService boardService;
	
	@MockBean
	private BoardDao boardDao;

	@Test
	public void testCreateNewBoard() {

		// given
		BDDMockito.given(boardDao.insertNewBoard(null)).willReturn(1);
		
		// when
		boolean isCreated = boardService.createNewBoard(null);
		
		// then
		assertTrue(isCreated);
	}
}

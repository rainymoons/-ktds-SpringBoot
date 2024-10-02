package com.ktdsuniversity.edu.hello_spring.bbs.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;

import com.ktdsuniversity.edu.hello_spring.bbs.dao.BoardDao;
import com.ktdsuniversity.edu.hello_spring.bbs.dao.impl.BoardDaoImpl;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteBoardVO;

@MybatisTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(BoardDaoImpl.class)
public class BoardDaoImplTest {

	@Autowired
	BoardDao boardDao;
	
	@Test
	public void testInsertNewBoard() {
		WriteBoardVO writeBoardVO = new WriteBoardVO();
		writeBoardVO.setContent("하잉");
		writeBoardVO.setEmail("naver@naver.com");
		writeBoardVO.setSubject("누구세요");
		
		int insertCount = boardDao.insertNewBoard(writeBoardVO);
//		assertEquals(count, 1);
		assertTrue(insertCount == 1);
	}
	
}

package com.ktdsuniversity.edu.hello_spring.bbs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.ktdsuniversity.edu.hello_spring.bbs.service.BoardService;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardListVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteBoardVO;

@Controller
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	@GetMapping("/board/list") // http://localhost:8080/board/list
	public String viewBoardList(Model model) {

		BoardListVO boardListVO = this.boardService.getAllBoard();

		model.addAttribute("boardListVO", boardListVO);

		return "board/boardList";
	}

	// write 페이지 작성.
	// getMapping은 post가 안됨. 그렇다고 post로 바꾸면 get에 대한 요청을 처리할 수 없다. -> 별도로 PostMapping을 생성함.
	@GetMapping("/board/write")
	public String viewBoardWrtiePage() {
		return "board/boardWrite";
	}

	@PostMapping("/board/write")
	public String doCreateNewBoard(WriteBoardVO writeBoardVO) {
		boolean isCreate = this.boardService.createNewBoard(writeBoardVO);
		System.out.println("게시글 등록 결과: " + isCreate);
		return "redirect:/board/list";
	}
}

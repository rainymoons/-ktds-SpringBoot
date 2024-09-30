package com.ktdsuniversity.edu.hello_spring.bbs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ktdsuniversity.edu.hello_spring.bbs.service.BoardService;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardListVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ModifyBoardVO;
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
	
	@GetMapping("/board/view")
	public String viewOneBoard(@RequestParam int id, Model model) {
		
		BoardVO boardVO = this.boardService.getOneBoard(id, true);
		
		model.addAttribute("boardVO", boardVO);
		
		return "board/boardView";
	}
	
	@GetMapping("/board/modify/{id}")
	public String viewBoardModifyPage(@PathVariable int id, Model model) {
		
		BoardVO boardVO = this.boardService.getOneBoard(id, false);
		
		model.addAttribute("boardVO", boardVO);
		
		return "board/boardModify";
		
	}
	
	// form에는 제목, 이메일, 내용만 있음. 아이디가 없으므로 id를 전달해줘야함.
	@PostMapping("/board/modify/{id}")
	public String doModifyOneBoard(@PathVariable int id, ModifyBoardVO modifyBoardVO, Model model) {
		modifyBoardVO.setId(id);
		
		boolean isUpdated = this.boardService.updateOneBoard(modifyBoardVO);
		
		//TODO post update process
		if(isUpdated) {
			// 성공적으로 수정했다면, 수정한 게시글의 상세조회 페이지로 이동시킨다.
			return "redirect:/board/view?id=" + id;
		}
		else {
			// TODO 사용자가 작성했던 내용을 JSP에 그대로 보내준다.
			model.addAttribute("boardVO", modifyBoardVO);
			return "board/boardmodify";
		}
		
	}
	
	// post가 아닌 get인 이유
	@GetMapping("/board/delete/{id}")
	public String doDeleteBoard(@PathVariable int id) {
		// 삭제 여부
		boolean isDeleted = this.boardService.deleteOneBoard(id);
		
		if(isDeleted) {
			// 삭제 성공시 list 출력
			return "redirect:/board/list";
		}
		else {
			// 삭제 실패시 원래 페이지로 리다이렉트
			return "redirect:/board/view?id" + id;
		}
		
	}
}

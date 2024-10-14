package com.ktdsuniversity.edu.hello_spring.bbs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ktdsuniversity.edu.hello_spring.bbs.service.BoardService;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardListVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ModifyBoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteBoardVO;
import com.ktdsuniversity.edu.hello_spring.common.beans.FileHandler;

import jakarta.validation.Valid;

@Controller
public class BoardController {

	@Autowired
	private FileHandler fileHandler;
	
	@Autowired
	private BoardService boardService;
	
	@GetMapping("/board/list") // http://localhost:8080/board/list
	public String viewBoardList(Model model) {
		BoardListVO boardListVO = this.boardService.getAllBoard();
		model.addAttribute("boardListVO", boardListVO);
		return "board/boardlist";
	}
	
	@GetMapping("/board/write") // http://localhost:8080/board/write
	public String viewBoardWritePage() {
		return "board/boardwrite";
	}
		
//	@PostMapping("/board/write")
//	public String doSomething(WriteBoardVO writeBoardVO) {
//		System.out.println("제목" + writeBoardVO.getSubject());
//		System.out.println("이메일" + writeBoardVO.getEmail());
//		System.out.println("내용" + writeBoardVO.getContent());
//		return "";
//	}
	
	@PostMapping("/board/write")
	public String doCreatNewBoard(@Valid WriteBoardVO writeBoardVO // @Valid WriteBoardVO의 Validation Check 수행
								, BindingResult bindingResult // @Valid의 실패 결과만 할당
								, Model model
								) {
		if(bindingResult.hasErrors()) { // 에러가 존재한다면
			model.addAttribute("writeBoardVO", writeBoardVO);
			return "board/boardwrite";
		}
		
		boolean isCreate = this.boardService.creatNewBoard(writeBoardVO);
		System.out.println("게시글 등록 결과 : " + isCreate);
		return "redirect:/board/list";
	}
	
	@GetMapping("/board/view") // http://localhost:8080/board/view?id=?
	public String viewOneBoard(Model model, @RequestParam int id) {
		BoardVO boardVO = this.boardService.getOneBoard(id, true);
		model.addAttribute("boardVO", boardVO);
		return "board/boardview";
	}
	
	@GetMapping("/board/modify/{id}") // http://localhost:8080/board/modify/?
	public String viewBoardModifyPage(Model model, @PathVariable int id) {
		BoardVO boardVO = this.boardService.getOneBoard(id, false);
		model.addAttribute("boardVO", boardVO);
		return "board/boardmodify";
	}
	
	@PostMapping("/board/modify/{id}")
	public String doModifyOneBoard(@PathVariable int id 
									, @Valid ModifyBoardVO modifyBoardVO
									, BindingResult bindingResult
									, Model model) {
		modifyBoardVO.setId(id); // 쿼리에서 아이디를 불러오기 위해 파라미터로 받아온 id를 넣어줘야 함
		if (bindingResult.hasErrors()) {
			model.addAttribute("boardVO", modifyBoardVO);
			return "board/boardmodify";
		}
		boolean isUpdated = this.boardService.updateOneBoard(modifyBoardVO);
		if (isUpdated) {
			return "redirect:/board/view?id=" + id;
		}
		else {
			// 사용자가 작성했던 내용을 JSP에 그대로 보내줌
			model.addAttribute("boardVO", modifyBoardVO);
			return "board/boardmodify";
		}
	}
	
	@GetMapping("/board/delete/{id}")
	public String doDeleteOneBoard(@PathVariable int id) {
		boolean isDeleted = this.boardService.deleteOneBoard(id);
		System.out.println("삭제 결과 : " + isDeleted);
		if (isDeleted) {
			return "redirect:/board/list";
		}
		else {
			// 삭제에 실패했다면 조회페이지로 이동
			return "redirect:/board/view?id=" + id;
		}
	}
	
	@GetMapping("/board/file/download/{id}") // import org.springframework.core.io.Resource;
	public ResponseEntity<Resource> doDownloadFile(@PathVariable int id) {
		// 1. 다운로드 할 파일의 이름을 알기 위해 게시글을 조회
		BoardVO boardVO = this.boardService.getOneBoard(id, false);
		
		return this.fileHandler.downloadFile(boardVO.getFileName(), boardVO.getOriginFileName());
	}
	
}

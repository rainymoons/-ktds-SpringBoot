package com.ktdsuniversity.edu.hello_spring.bbs.web;


import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ktdsuniversity.edu.hello_spring.bbs.service.BoardService;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardListVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.DeleteBoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ModifyBoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteBoardVO;
import com.ktdsuniversity.edu.hello_spring.common.beans.FileHandler;
import com.ktdsuniversity.edu.hello_spring.member.vo.MemberVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class BoardController {

	public static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
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
								, @SessionAttribute(value = "_LOGIN_USER_", required = false) MemberVO loginMemberVO) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		writeBoardVO.setIp( request.getRemoteAddr() );
		
		if(bindingResult.hasErrors()) { // 에러가 존재한다면
			model.addAttribute("writeBoardVO", writeBoardVO);
			return "board/boardwrite";
		}
		
		// MemberVO memberVO = (MemberVO)session.getAttribute("_LOGIN_USER_"); @SessionAttribute로 대체

		if (loginMemberVO == null) {
			return "redirect:/member/login";
		}
		// session 에서 가져온 email
		writeBoardVO.setEmail( loginMemberVO.getEmail() );
		
		boolean isCreate = this.boardService.creatNewBoard(writeBoardVO);
		if (logger.isDebugEnabled()) {
			
			logger.debug("게시글 등록 결과 : " + isCreate);
		}
		return "redirect:/board/list";
	}
	
	@GetMapping("/board/view") // http://localhost:8080/board/view?id=?
	public String viewOneBoard(Model model, @RequestParam int id) {
		BoardVO boardVO = this.boardService.getOneBoard(id, true);
		model.addAttribute("boardVO", boardVO);
		return "board/boardview";
	}
	
	// 자신이 작성한 게시글이 아닌 경우 수정 불가.
	@GetMapping("/board/modify/{id}") // http://localhost:8080/board/modify/?
	public String viewBoardModifyPage(Model model, @PathVariable int id,
									  @SessionAttribute("_LOGIN_USER_") MemberVO memberVO) {
		
		// 수정페이지에 보여줄 게시글 조회- > 사용자가 같지 않다면, 예외 -> 수정페이지를 보여주지 않는다.
		BoardVO boardVO = this.boardService.getOneBoard(id, false);
		
		if ( !boardVO.getEmail().equals(memberVO.getEmail())) {
			throw new IllegalArgumentException("잘못된 접근입니다.");
		}
		
		model.addAttribute("boardVO", boardVO);
		return "board/boardmodify";
	}
	
	// 자신이 작성한 게시물이 아닐 경우 수정 불가.
	@PostMapping("/board/modify/{id}")
	public String doModifyOneBoard(@PathVariable int id 
									, @Valid ModifyBoardVO modifyBoardVO
									, BindingResult bindingResult
									, Model model
									, @SessionAttribute(value = "_LOGIN_USER_", required = false) MemberVO loginMemberVO) {
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		modifyBoardVO.setIp(request.getRemoteAddr());
		
		modifyBoardVO.setId(id); // 쿼리에서 아이디를 불러오기 위해 파라미터로 받아온 id를 넣어줘야 함
		if (bindingResult.hasErrors()) {
			model.addAttribute("boardVO", modifyBoardVO);
			return "board/boardmodify";
		}
		
		if (loginMemberVO == null) {
			return "redirect:/member/login";
		}
		
		modifyBoardVO.setEmail( loginMemberVO.getEmail() );
		
		// 이메일 이 다르면.
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
	
	// 자신이 작성한 게시글이 아닌 경우 삭제 불가.
	@GetMapping("/board/delete/{id}")
	public String doDeleteOneBoard(@PathVariable int id, @SessionAttribute("_LOGIN_USER_") MemberVO memberVO) {
		
		DeleteBoardVO deleteBoardVO = new DeleteBoardVO();
		deleteBoardVO.setId(id);
		deleteBoardVO.setEmail(memberVO.getEmail());
		
		boolean isDeleted = this.boardService.deleteOneBoard(deleteBoardVO);
		
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
	
	@GetMapping("/member/logout")
	public String doLogout(HttpSession session) {
		// 로그인상태를 무효화 -> Session 이 날아감
		session.invalidate();
		return "redirect:/board/list";
	}
	
	@GetMapping("/board/excel/download")
	public ResponseEntity<Resource> doDownloadExcel() {
		
		// 1. WorkBook (엑셀 워크시트) 생성
		Workbook workBook = new SXSSFWorkbook(-1); // SXSSF가 .xlsx 포멧의 워크북을 생성함.
		
		// 2. WorkBook에 Sheet 만들기
		Sheet sheet = workBook.createSheet("게시글 목록");
			// 이 상태는 내부에 아무 칸이 없는 상태.
		
		// 3. Sheet에 Row 만들기
		Row row = sheet.createRow(0); // 1번 줄을 생성. 칸은 존재하지 않는다.
		
		// 4. Row에 Cell 만들기 (칸을 만드는 것)
		Cell cell = row.createCell(0); // 1A 칸을 생성함. 네모 한칸.
		cell.setCellValue("번호");
		
		cell = row.createCell(1); // 재할당. B1 생성.
		cell.setCellValue("제목");
		
		cell = row.createCell(2); // c1
		cell.setCellValue("첨부파일명");
		
		cell = row.createCell(3); // d1
		cell.setCellValue("작성자이메일");
		
		cell = row.createCell(4); // e1
		cell.setCellValue("조회수");
		
		cell = row.createCell(5); // f1
		cell.setCellValue("등록일");
		
		cell = row.createCell(6); // g1
		cell.setCellValue("수정일");
		
		// 게시글 조회
		BoardListVO boardListVO = this.boardService.getAllBoard();
		List<BoardVO> boardList = boardListVO.getBoardList();
		
		int rowIndex = 1;
		for (BoardVO boardVO : boardList) {
			// sheet에 Row 만들기
			row = sheet.createRow(rowIndex++);
			
			// Row에 Cell 만들기
			cell = row.createCell(0);
			cell.setCellValue(boardVO.getId() + ""); // id: 112일 경우 엑셀에는 112.0으로 표시. 왜? setCellValue가 double타입이므로.
			cell = row.createCell(1);
			cell.setCellValue(boardVO.getSubject());
			cell = row.createCell(2);
			cell.setCellValue(boardVO.getOriginFileName());
			cell = row.createCell(3);
			cell.setCellValue(boardVO.getEmail());
			cell = row.createCell(4);
			cell.setCellValue(boardVO.getViewCnt() + "");
			cell = row.createCell(5);
			cell.setCellValue(boardVO.getCrtDt());
			cell = row.createCell(6);
			cell.setCellValue(boardVO.getMdfyDt());
		}
		
		// 5. WorkBook을 file로 생성 (fileHandler에게 WorkBook을 줄테니 file을 달라)
		
		String excelFileName = this.fileHandler.createXlsxFile(workBook);
		
		// 6. File을 다운로드.
		return this.fileHandler.downloadFile(excelFileName, "게시글 목록.xlsx");
	}
}

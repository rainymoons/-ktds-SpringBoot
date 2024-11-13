package com.ktdsuniversity.edu.hello_spring.bbs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ktdsuniversity.edu.hello_spring.bbs.service.BoardService;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardListVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.SearchBoardVO;
import com.ktdsuniversity.edu.hello_spring.common.vo.ApiResponse;

@RestController
@RequestMapping("/api/v1")
public class BoardApiController {
	
	@Autowired
	private BoardService boardService;
	
	
	/**
	 * JSP : GET / POST(form을 쓰는 경우)
	 * 
	 * API
	 *   - GET : API 데이터 조회하는 역할 (@GetMapping)  -> QueryString Parameter를 받음. (@RequestParam, CommandObject)
	 *   - POST : API 데이터를 생성하는 역할 (@PostMapping)  ->  JSON request (JSON으로 리퀘스트를 받을 수 있음) -> (@RequsetBody만 사용 가능)
	 *   - PUT(FETCH) : API 데이터를 수정하는 역할 (@PutMapping)  -> JSON request (JSON으로 리퀘스트를 받을 수 있음) -> (@RequsetBody만 사용 가능)
	 *   - DELETE : API 데이터 삭제하는 역할 (@DeleteMapping)  -> QueryString Parameter를 받음. (@RequestParam, CommandObject)
	 */
	@GetMapping("/board/list") 
	public ApiResponse viewBoardList(SearchBoardVO searchBoardVO) { // @RequesetBody 쓰지 않는 이유. GetMapping은 queryString Parameter로만 데이터를 응답받을 수 있음)
		BoardListVO boardListVO = this.boardService.getAllBoard(searchBoardVO);
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setBody(boardListVO.getBoardList()); // count는 바디의 리스트의 갯수만큼 채워짐
		return apiResponse;
	}
}

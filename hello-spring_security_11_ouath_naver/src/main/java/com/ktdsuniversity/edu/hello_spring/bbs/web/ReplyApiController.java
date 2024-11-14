package com.ktdsuniversity.edu.hello_spring.bbs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ktdsuniversity.edu.hello_spring.bbs.service.ReplyService;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteReplyVO;
import com.ktdsuniversity.edu.hello_spring.common.utils.PrincipalUtil;
import com.ktdsuniversity.edu.hello_spring.common.vo.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class ReplyApiController {

	@Autowired
	private ReplyService replyService;
	
	@PostMapping("/board/reply/{boardId}")
	public ApiResponse doCreateNewReplies(@PathVariable int boardId,
										  @RequestBody
										  @Valid WriteReplyVO writeReplyVO,
										  BindingResult bindingResult,
										  Authentication authentication) {
		if (bindingResult.hasErrors()) {
			// bindingResult 내부에 있는 에러메세지들만 모아서 보낸다.
			List<String> fieldErrors = bindingResult.getFieldErrors()
													.stream()
													.map(fieldError -> fieldError.getDefaultMessage())
													.toList();
			ApiResponse errorResponse = new ApiResponse(HttpStatus.BAD_REQUEST);
			errorResponse.setErrors(fieldErrors);
			return errorResponse;
		}
		
		writeReplyVO.setBoardId(boardId);
		writeReplyVO.setEmail(PrincipalUtil.email(authentication));
		boolean isCreated = replyService.createNewReply(writeReplyVO);
		
		return new ApiResponse(isCreated);
	}

}

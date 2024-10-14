package com.ktdsuniversity.edu.hello_spring.bbs.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.ktdsuniversity.edu.hello_spring.bbs.service.ReplyService;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ModifyReplyVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ReplyVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteReplyVO;
import com.ktdsuniversity.edu.hello_spring.member.vo.MemberVO;

@RestController
public class ReplyController {

	@Autowired
	private ReplyService replyService;
	
	@GetMapping("/board/reply/{boardId}")
	public Map<String, Object> getAllReplies(@PathVariable int boardId) {
		List<ReplyVO> replyList = replyService.getAllReplies(boardId);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("count", replyList.size());
		resultMap.put("replies", replyList);
		return resultMap;
	}
	
	@PostMapping("/board/reply/{boardId}")
	public Map<String, Object> doCreateNewReplies(@PathVariable int boardId,
												  @ModelAttribute WriteReplyVO writeReplyVO,
												  @SessionAttribute("_LOGIN_USER_") MemberVO memberVO) {
		writeReplyVO.setBoardId(boardId);
		writeReplyVO.setEmail(memberVO.getEmail());
		boolean isSuccess = replyService.createNewReply(writeReplyVO);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("result", isSuccess);
		return resultMap;
	}
	
	@GetMapping("/board/reply/delete/{replyId}")
	public Map<String, Object> doDeleteReplies(@PathVariable int replyId, 
											   @SessionAttribute("_LOGIN_USER_") MemberVO memberVO) {
		boolean isSuccess = replyService.deleteOneReply(replyId, memberVO.getEmail());
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("result", isSuccess);
		return resultMap;
	}
	
	@PostMapping("/board/reply/modify/{replyId}")
	public Map<String, Object> doUpdateReplies(@PathVariable int replyId, 
			                                   @ModelAttribute ModifyReplyVO modifyReplyVO, 
			                                   @SessionAttribute("_LOGIN_USER_") MemberVO memberVO) {
		modifyReplyVO.setReplyId(replyId);
		modifyReplyVO.setEmail(memberVO.getEmail());
		boolean isSuccess = replyService.updateOneReply(modifyReplyVO);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("result", isSuccess);
		return resultMap;
	}
	
	@GetMapping("/board/reply/recommend/{replyId}")
	public Map<String, Object> doRecommendReplies(@PathVariable int replyId, 
												  @SessionAttribute("_LOGIN_USER_") MemberVO memberVO) {
		boolean isSuccess = replyService.recommendOneReply(replyId, memberVO.getEmail());
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("result", isSuccess);
		return resultMap;
	}
}

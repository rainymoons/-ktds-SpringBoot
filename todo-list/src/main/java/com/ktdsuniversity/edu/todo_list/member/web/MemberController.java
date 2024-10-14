package com.ktdsuniversity.edu.todo_list.member.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.ktdsuniversity.edu.todo_list.member.service.MemberService;
import com.ktdsuniversity.edu.todo_list.member.vo.LoginMemberVO;
import com.ktdsuniversity.edu.todo_list.member.vo.MemberVO;
import com.ktdsuniversity.edu.todo_list.member.vo.RegistMemberVO;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	@GetMapping("/member/regist")
	public String viewRegistPage() {
		return "member/memberregist";
	}
	
	@PostMapping("/member/regist")
	public String doCreateMember(@Valid RegistMemberVO registMemberVO,
			                    BindingResult bindingResult,
			                    Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("registMemberVO", registMemberVO);
			return "member/memberregist";
		}
		if (!registMemberVO.getPassword().equals(registMemberVO.getConfirmPassword())) {
			model.addAttribute("confirmError", "비밀번호가 일치하지 않습니다.");
			model.addAttribute("registMemberVO", registMemberVO);
			return "member/memberregist";
		}
		try {
			this.memberService.createNewMember(registMemberVO);
		}
		catch (IllegalArgumentException iae) {
			model.addAttribute("emailError", iae.getMessage());
			model.addAttribute("registMemberVO", registMemberVO);
			return "member/memberregist";
		}
		return "redirect:/member/login";
	}
	
	@GetMapping("/member/login")
	public String viewLoginPage() {
		return "member/memberlogin";
	}
	
	@PostMapping("/member/login")
	public String doLogin(@Valid LoginMemberVO loginMemberVO
			             , BindingResult bindingResult
			             , HttpSession session
			             , Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("loginMemberVO", loginMemberVO);
			return "member/memberlogin";
		}
		try {
			MemberVO memberVO = this.memberService.readMember(loginMemberVO);
			session.setAttribute("_LOGIN_USER_", memberVO);
		}
		catch (IllegalArgumentException iae) {
			model.addAttribute("loginMemberVO", loginMemberVO);
			model.addAttribute("message", iae.getMessage());
			return "member/memberlogin";
		}
		return "redirect:/todo/list";
	}
	
	@ResponseBody
	@GetMapping("/member/regist/available") // ajax 요청에 사용되는 url
	public Map<String, Object> doCheckAvailableEmail(@RequestParam String email) {
		boolean isAvailableEmail = this.memberService.checkAvailableEmail(email);
		
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("email", email);
		response.put("available", isAvailableEmail);
		
		return response;
	}
	
	@GetMapping("/member/logout")
	public String doLogout(HttpSession session) {
		session.invalidate();
		return "redirect:/todo/list";
	}
	
	@GetMapping("/member/delete-me")
	public String doDeleteMember(@SessionAttribute("_LOGIN_USER_") MemberVO loginMemberVO
								, HttpSession session) {
		boolean isDeleted = this.memberService.deleteMember(loginMemberVO.getEmail());
		if (!isDeleted) {
			return "redirect:/member/fail-delete-me";
		}
		session.invalidate();
		return "redirect:/member/success-delete-me";
	}
	
	@GetMapping("/member/{result}-delete-me")
	public String viewDeletePage(@PathVariable String result) {
		result = result.toLowerCase();
		if (!result.equals("fail") && !result.equals("success")) {
			return "error/404";
		}
		return "member/" + result + "delete";
	}
	
}

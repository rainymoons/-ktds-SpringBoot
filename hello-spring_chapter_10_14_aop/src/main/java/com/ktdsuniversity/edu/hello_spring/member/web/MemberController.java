package com.ktdsuniversity.edu.hello_spring.member.web;

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

import com.ktdsuniversity.edu.hello_spring.member.service.MemberService;
import com.ktdsuniversity.edu.hello_spring.member.vo.LoginMemberVO;
import com.ktdsuniversity.edu.hello_spring.member.vo.MemberVO;
import com.ktdsuniversity.edu.hello_spring.member.vo.RegistMemberVO;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	@GetMapping("/member/regist") // http://localhost:8080/member/regist
	public String viewCreateMemberPage() {
		return "member/memberregist";
	}
	
	@PostMapping("/member/regist")
	public String doCreateNewMember(@Valid RegistMemberVO registMemberVO
									, BindingResult bindingResult
									, Model model) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("memberVO", registMemberVO);
			return "member/memberregist";
		}
		
		if(!registMemberVO.getConfirmPassword().equals(registMemberVO.getPassword())) {
			model.addAttribute("error_confirmPassword", "비밀번호가 일치하지 않습니다.");
			model.addAttribute("memberVO", registMemberVO);
			return "member/memberregist";
		}
		
		boolean isCreated = this.memberService.createNewMember(registMemberVO);
		System.out.println(isCreated);
		return "redirect:/member/login";
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
			// .memberService.readMember 호출해서 사용자가 입력한 loginMemberVO를 기반으로 회원 정보 조회
			// 아이디와 비밀번호가 유효하면 MemberVO객체 반환(사용자 정보 담긴)
			MemberVO memberVO = this.memberService.readMember(loginMemberVO);
			// 사용자의 세션에 로그인 정보를 저장함.(memberVO) -> 다른 로그인이 필요한 요청에서도 로그인 상태 유지.
			session.setAttribute("_LOGIN_USER_", memberVO);

/*
		// 로그인이 실패하는 경우.
		catch (IllegalArgumentException iae) {
			// 여기서 memberVO가 아니라 loginMemberVO인 이유?
			model.addAttribute("loginMemberVO", loginMemberVO);
			// 예외 메세지를 view로 전달.
			model.addAttribute("message", iae.getMessage());
			// 로그인에 실패했으므로 다시 로그인페이지로 렌더링
			return "member/memberlogin";
		}
*/	
		//로그인 성공 시 특정 URL로 리다이렉트.
		// nextUrl은 어떻게 아는가? -> JQuery를 이용해서.(memberLogin.jsp & memberLogin.js)
		return "redirect:" + loginMemberVO.getNextUrl(); // 우리가 넣어주는 url로 이동을 하게 됨.
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
		return "member/" + result + "deleteme";
	}
	
	
}

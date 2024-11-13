package com.ktdsuniversity.edu.hello_spring.member.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ktdsuniversity.edu.hello_spring.common.utils.PrincipalUtil;
import com.ktdsuniversity.edu.hello_spring.member.service.MemberService;
import com.ktdsuniversity.edu.hello_spring.member.vo.RegistMemberVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
	public String doCreateNewMember(@Valid RegistMemberVO registMemberVO, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("memberVO", registMemberVO);
			return "member/memberregist";
		}

		if (!registMemberVO.getConfirmPassword().equals(registMemberVO.getPassword())) {
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

	@GetMapping("/member/delete-me")
	public String doDeleteMember(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
		boolean isDeleted = this.memberService.deleteMember(PrincipalUtil.email(authentication)); // getName이 email
		if (!isDeleted) {

			LogoutHandler logoutHandler = new SecurityContextLogoutHandler();
			logoutHandler.logout(request, response, authentication);

			return "redirect:/member/fail-delete-me";
		}
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

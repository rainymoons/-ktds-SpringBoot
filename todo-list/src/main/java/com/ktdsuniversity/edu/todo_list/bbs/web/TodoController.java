package com.ktdsuniversity.edu.todo_list.bbs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.ktdsuniversity.edu.todo_list.bbs.service.TodoService;
import com.ktdsuniversity.edu.todo_list.bbs.vo.DeleteTodoVO;
import com.ktdsuniversity.edu.todo_list.bbs.vo.TodoListVO;
import com.ktdsuniversity.edu.todo_list.bbs.vo.TodoVO;
import com.ktdsuniversity.edu.todo_list.bbs.vo.WriteTodoVO;
import com.ktdsuniversity.edu.todo_list.common.utils.RequestUtil;
import com.ktdsuniversity.edu.todo_list.member.vo.MemberVO;

@Controller
public class TodoController {

	DeleteTodoVO deleteTodoVO;
	
	@Autowired
	private TodoService todoService;
	
	@GetMapping("/todo/list") // http://localhost:8080/todo/list
	public String viewTodoList(Model model) {
		TodoListVO todoListVO = this.todoService.getAllTodo();
		model.addAttribute("todoListVO", todoListVO);
		return "todo/todolist";
	}
	
	@GetMapping("/todo/write") // http://localhost:8080/todo/write
	public String addTodo() {
		return "todo/todowrite";
	}
	
	@PostMapping("/todo/write")
	public String doCreateTodo(WriteTodoVO writeTodoVO
			                 , Model model
			                 , @SessionAttribute(value = "_LOGIN_USER_", required = false) MemberVO loginMemberVO) {
		
		writeTodoVO.setIp(RequestUtil.getIp());
		if (loginMemberVO == null) {
			return "redirect:/member/login";
		}
		
		boolean isCreate = this.todoService.creatNewTodo(writeTodoVO);
		if (isCreate) {
			return "redirect:/todo/list";
		}
		else {
			model.addAttribute("todoVO", writeTodoVO);
			return "todo/todowrite";
		}
	}
	
	@GetMapping("/todo/complete{id}")
	public String doChangeIsComplete(@PathVariable int id, TodoVO todoVO) {
		boolean isComplete = this.todoService.changeIsComplete(todoVO);
		// 성공하면 list로
		if (isComplete) {
			return "redirect:/todo/list";
		}
		return "redirect:/todo/list";
	}
	
	@GetMapping("/todo/delete{id}")
	public String deleteTodo(@PathVariable int id, 
			@SessionAttribute(value = "_LOGIN_USER_", required = false) TodoVO todoVO) {
	
		DeleteTodoVO deleteTodoVO = new DeleteTodoVO();
		deleteTodoVO.setId(id);
		
		boolean isDeleted = this.todoService.deleteTodo(id);
		if(isDeleted) {
			return "redirect:/todo/list";
		}
		return "redirect:/todo/list";
	}
	
}

package com.ktdsuniversity.edu.todo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ktdsuniversity.edu.todo.service.TodoService;
import com.ktdsuniversity.edu.todo.vo.TodoVO;
import com.ktdsuniversity.edu.todo.vo.WriteTodoVO;

@Controller
public class TodoController {
	
	@Autowired
	private TodoService todoService;
	
	@GetMapping("/todo/write")
	public String moveWriteForm() {
		return "todo/writeform";
	}

	@PostMapping("/todo/write")
	public String writeTodo(WriteTodoVO writeTodoVO) {
		this.todoService.createNewTodoItem(writeTodoVO);
		return "redirect:/todo/todolist";
	}
	
	@GetMapping("/todo/todolist")
	public String viewBoardList(Model model) {
		List<TodoVO> todolist = this.todoService.getTodoList();
		model.addAttribute("todolist", todolist);
		return "todo/todolist";
	}
	
	@GetMapping("/todo/delete/{id}")
	public String deleteTodo(@PathVariable int id) {
		this.todoService.deleteTodoItem(id);
		return "redirect:/todo/todolist";
	}
	
	@GetMapping("/todo/isdone/{id}")
	public String setStatus(@PathVariable int id) {
		this.todoService.alterStatus(id);
		return "redirect:/todo/todolist";
	}
}

package com.ktdsuniversity.edu.todo_list.bbs.service;

import com.ktdsuniversity.edu.todo_list.bbs.vo.TodoListVO;
import com.ktdsuniversity.edu.todo_list.bbs.vo.TodoVO;
import com.ktdsuniversity.edu.todo_list.bbs.vo.WriteTodoVO;

public interface TodoService {
	
	
	public TodoListVO getAllTodo();
	
	public boolean creatNewTodo(WriteTodoVO writeTodoVO);
	
	public boolean changeIsComplete(TodoVO TodoVO);
	
	public boolean deleteTodo(int id);
	
}

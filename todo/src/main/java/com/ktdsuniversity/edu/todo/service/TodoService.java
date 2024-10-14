package com.ktdsuniversity.edu.todo.service;

import java.util.List;

import com.ktdsuniversity.edu.todo.vo.TodoVO;
import com.ktdsuniversity.edu.todo.vo.WriteTodoVO;

public interface TodoService {

	public boolean createNewTodoItem(WriteTodoVO writeTodoVO);
	
	public boolean deleteTodoItem(int id);
	
	public List<TodoVO> getTodoList();
	
	public boolean alterStatus(int id);
}

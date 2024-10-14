package com.ktdsuniversity.edu.todo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktdsuniversity.edu.todo.dao.TodoDao;
import com.ktdsuniversity.edu.todo.service.TodoService;
import com.ktdsuniversity.edu.todo.vo.TodoVO;
import com.ktdsuniversity.edu.todo.vo.WriteTodoVO;

@Service
public class TodoServiceImpl implements TodoService {
	
	@Autowired
	private TodoDao todoDao;
	
	@Override
	public boolean createNewTodoItem(WriteTodoVO writeTodoVO) {
		int result = this.todoDao.insertNewTodoItem(writeTodoVO);
		return result > 0;
	}
	
	@Override
	public boolean deleteTodoItem(int id) {
		int result = this.todoDao.deleteTodoItem(id);
		return result > 0;
	}
	
	@Override
	public List<TodoVO> getTodoList() {
		List<TodoVO> todoVO = this.todoDao.selectAllToDo();
		return todoVO;
	}
	
	@Override
	public boolean alterStatus(int id) {
		int result = this.todoDao.updateTodoStatus(id);
		return result > 0;
	}
}

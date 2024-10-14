package com.ktdsuniversity.edu.todo_list.bbs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktdsuniversity.edu.todo_list.bbs.dao.TodoDao;
import com.ktdsuniversity.edu.todo_list.bbs.service.TodoService;
import com.ktdsuniversity.edu.todo_list.bbs.vo.DeleteTodoVO;
import com.ktdsuniversity.edu.todo_list.bbs.vo.TodoListVO;
import com.ktdsuniversity.edu.todo_list.bbs.vo.TodoVO;
import com.ktdsuniversity.edu.todo_list.bbs.vo.WriteTodoVO;

@Service
public class TodoServiceImpl implements TodoService {

	@Autowired
	private TodoDao todoDao;
	
	@Override
	public TodoListVO getAllTodo() {
		List<TodoVO> todoList = this.todoDao.selectAllTodo();
		
		TodoListVO todoListVO = new TodoListVO();
		todoListVO.setTodoList(todoList);
		
		return todoListVO;
	}
	
	@Override
	public boolean creatNewTodo(WriteTodoVO writeTodoVO) {
		int createCount = this.todoDao.insertNewTodo(writeTodoVO);
		return createCount > 0;
	}

	@Override
	public boolean changeIsComplete(TodoVO todoVO) {
		int updateCount = this.todoDao.updateTodo(todoVO);
		return updateCount > 0;
	}

	@Override
	public boolean deleteTodo(int id) {
		int deleteCount = this.todoDao.deleteTodo(id);
		return deleteCount > 0;
	}

}

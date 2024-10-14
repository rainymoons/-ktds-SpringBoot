package com.ktdsuniversity.edu.todo.dao;

import java.util.List;

import com.ktdsuniversity.edu.todo.vo.TodoVO;
import com.ktdsuniversity.edu.todo.vo.WriteTodoVO;

public interface TodoDao {

	public int insertNewTodoItem(WriteTodoVO writeTodoVO);
	
	public int deleteTodoItem(int id);
	
	public List<TodoVO> selectAllToDo();
	
	public int updateTodoStatus(int id);
}

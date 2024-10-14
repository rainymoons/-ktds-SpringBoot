package com.ktdsuniversity.edu.todo_list.bbs.dao;

import java.util.List;

import com.ktdsuniversity.edu.todo_list.bbs.vo.TodoVO;
import com.ktdsuniversity.edu.todo_list.bbs.vo.WriteTodoVO;

public interface TodoDao {

	public String NAMESPACE = "com.ktdsuniversity.edu.todo_list.bbs.dao.TodoDao";
	
	public List<TodoVO> selectAllTodo();
	
	public int insertNewTodo(WriteTodoVO writeTodoVO);
	
	/**
	 * 완료 버튼을 클릭하면 isComplete가 1로 변경
	 * @param id 게시글 ID
	 * @return
	 */
	public int updateTodo(TodoVO todoVO);
	
	/**
	 * 삭제버튼을 클릭하면 파라미터로 받아온 id의 Todo가 삭제
	 * @param id 게시글 ID
	 * @return
	 */
	public int deleteTodo(int id);
	
}

package com.ktdsuniversity.edu.todo.dao.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ktdsuniversity.edu.todo.dao.TodoDao;
import com.ktdsuniversity.edu.todo.vo.TodoVO;
import com.ktdsuniversity.edu.todo.vo.WriteTodoVO;

@Repository
public class TodoDaoImpl extends SqlSessionDaoSupport implements TodoDao {

	
	@Override
	@Autowired
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}
	
	@Override
	public int insertNewTodoItem(WriteTodoVO writeTodoVO) {
		return this.getSqlSession().insert("com.ktdsuniversity.edu.todo.dao.TodoDao.insertNewTodoItem", writeTodoVO);
	}
	
	@Override
	public int deleteTodoItem(int id) {
		return this.getSqlSession().delete("com.ktdsuniversity.edu.todo.dao.TodoDao.deleteTodoItem", id);
	}
	
	@Override
	public List<TodoVO> selectAllToDo() {
		return this.getSqlSession().selectList("com.ktdsuniversity.edu.todo.dao.TodoDao.selectAllToDo");
	}
	
	@Override
	public int updateTodoStatus(int id) {
		return this.getSqlSession().update("com.ktdsuniversity.edu.todo.dao.TodoDao.updateTodoStatus", id);
	}

}

package com.ktdsuniversity.edu.todo_list.access.dao;

import com.ktdsuniversity.edu.todo_list.access.vo.AccessLogVO;

public interface AccessLogDao {

	public String NAMESPACE = "com.ktdsuniversity.edu.todo_list.access.dao.AccessLogDao";
	
	public int insertNewAccessLog(AccessLogVO accessLogVO);
	
	public int selectLoginFailCount(String ip);
	
}

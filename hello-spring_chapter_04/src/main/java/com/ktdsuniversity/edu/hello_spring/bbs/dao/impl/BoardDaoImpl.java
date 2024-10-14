package com.ktdsuniversity.edu.hello_spring.bbs.dao.impl;

import com.ktdsuniversity.edu.hello_spring.bbs.dao.BoardDao;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardVO;
import java.util.List;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BoardDaoImpl extends SqlSessionDaoSupport implements BoardDao {

    @Override
    @Autowired
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }

    @Override
    public int selectBoardAllCount() {
        return this.getSqlSession().selectOne("com.ktdsuniversity.edu.hello_spring.bbs.BoardDao");
    }

    @Override
    public List<BoardVO> selectAllBoard() {
        return this.getSqlSession().selectList("com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardVO");
    }
}

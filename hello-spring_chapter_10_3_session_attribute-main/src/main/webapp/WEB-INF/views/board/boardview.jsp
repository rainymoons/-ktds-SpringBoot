<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>게시글 조회</title>
    <link rel="stylesheet" type="text/css" href="/css/common.css" />
  </head>
  <body>
    <h1>${boardVO.id}번 게시글 조회</h1>
    <div class="grid grid-view-board">
      <label for="subject">제목</label>
      <div>${boardVO.subject}</div>
      <label for="email">이메일</label>
      <div>${boardVO.email}</div>
      <label for="viewCnt">조회수</label>
      <div>${boardVO.viewCnt}</div>
      <label for="originFileName">첨부파일</label>
      <div>
        <a href="/board/file/download/${boardVO.id}">
          ${boardVO.originFileName}
        </a>
      </div>
      <label for="crtDt">등록일</label>
      <div>${boardVO.crtDt}</div>
      <label for="mdfyDt">수정일</label>
      <div>${boardVO.mdfyDt}</div>
      <label for="content">내용</label>
      <div>${boardVO.content}</div>
      <div class="btn-group">
        <div class="right-align">
          <a href="/board/modify/${boardVO.id}">수정</a>
          <a href="/board/delete/${boardVO.id}">삭제</a>
        </div>
      </div>
    </div>
  </body>
</html>

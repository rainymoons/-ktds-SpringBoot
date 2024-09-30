<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Insert title here</title>
  </head>
    <link rel="stylesheet" type="text/css" href="/css/common.css" />
  <body>
    <h1>게시글 수정</h1>
    <form method="post" action="/board/modify/${boardVO.id}">

      <div class="grid">
        <label for="subject">제목</label>
        <input
          id="subject"
          type="text"
          name="subject"
          value="${boardVO.subject}"
        />
        <label for="email">이메일</label>
        <input type="email" id="email" name="email" value="${boardVO.email}"/>
        <label for="content">내용</label>
        <textarea name="content" id="content">${boardVO.content}</textarea>
        <div class="btn-group">
          <div class="right-align">
            <input type="submit" value="수정" />
          </div>
        </div>
      </div>
    </form>
  </body>
</html>

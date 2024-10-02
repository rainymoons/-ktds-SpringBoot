<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Insert title here</title>
  </head>
  <link rel="stylesheet" type="text/css" href="/css/common.css" />
  <body>
    <h1>게시글 수정</h1>
    <form:form modelAttribute="modifyBoardVO"
      method="post"
      action="/board/modify/${boardVO.id}"
      enctype="multipart/form-data"
    >
      <div class="grid">
      <div>
        <label for="subject">제목</label>
        <form:errors path="subject" element="div" cssClass="error" />
        <input
          id="subject"
          type="text"
          name="subject"
          value="${boardVO.subject}"
        />
        </div>
        
        <div>
        <label for="email">이메일</label>
        <form:errors path="email" element="div" cssClass="error" />
        <input type="email" id="email" name="email" value="${boardVO.email}" />
		</div>
		
        <label for="file">첨부파일</label>
        <input id="file" type="file" name="file" />

        <label for="content">내용</label>
        <textarea name="content" id="content">${boardVO.content}</textarea>
        <div class="btn-group">
          <div class="right-align">
            <input type="submit" value="수정" />
          </div>
        </div>
      </div>
    </form:form>
  </body>
</html>

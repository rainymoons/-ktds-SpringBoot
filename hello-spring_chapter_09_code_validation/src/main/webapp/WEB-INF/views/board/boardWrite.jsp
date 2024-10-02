<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>게시글 작성하기</title>
    <link rel="stylesheet" type="text/css" href="/css/common.css" />
  </head>
  <body>
    <h1>게시글 작성</h1>
    <form method="post" enctype="multipart/form-data">
      <div class="errors">
        <p>${error_subject}</p>
        <p>${error_email}</p>
      </div>

      <div class="grid">
        <label for="subject">제목</label>
        <input
          id="subject"
          type="text"
          name="subject"
          value="${writeBoardVO.subject}"
        />

        <label for="email">이메일</label>
        <input
          id="email"
          type="email"
          name="email"
          value="${writeBoardVO.email}"
        />

        <label for="file">첨부파일</label>
        <input id="file" type="file" name="file" />

        <label for="content">내용</label>
        <textarea id="content" name="content">${writeBoardVO.content}</textarea>
        <div class="btn-group">
          <div class="right-align">
            <!-- submit : 서버로 전송 -->
            <input type="submit" value="저장" />
          </div>
        </div>
      </div>
    </form>
  </body>
</html>

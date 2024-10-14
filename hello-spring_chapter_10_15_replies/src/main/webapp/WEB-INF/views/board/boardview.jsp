<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>게시글 조회</title>
    <link rel="stylesheet" type="text/css" href="/css/common.css" />
    <script type="text/javascript" src="/js/jquery-3.7.1.min.js"></script>
	<script type="text/javascript" src="/js/board/reply.js"></script>

  </head>
  <body>
    <h1>${boardVO.id}번 게시글 조회</h1>
    <div class="grid grid-view-board" data-board-id="${boardVO.id}"> <!-- 몇번 게시글을 보고 있는지 알고 싶어서 설정. 어디서? reply.js에서 -->
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
      
      <div class="replies"></div>
      	<!-- 댓글의 목록이 노출된다. -->
      	<div class="reply-items"></div>
      	<!-- 댓글을 작성하는 양식을 보여준다. -->
      	<div class="write-reply">
      		<textarea class="txt-reply"></textarea>
      		<button id="btn-save-reply">등록</button>
      		<button id="btn-cancel-reply">취소</button>
    	</div>
      <div class="btn-group">
      <c:if test="${sessionScope._LOGIN_USER_.email eq boardVO.email}" >
        <div class="right-align">
          <a href="/board/list">목록으로</a>
          <a href="/board/modify/${boardVO.id}">수정</a>
          <a href="/board/delete/${boardVO.id}">삭제</a>
        </div>
        </c:if>
      </div>
    </div>
  </body>
</html>

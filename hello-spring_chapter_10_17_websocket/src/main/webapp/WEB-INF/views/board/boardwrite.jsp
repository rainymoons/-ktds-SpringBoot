<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>게시글 작성하기</title>
    <link rel="stylesheet" type="text/css" href="/css/common.css" />
    <script type="text/javascript" src="/js/jquery-3.7.1.min.js"></script>
    <jsp:include page="../common/websocket_script.jsp" />
  </head>
  <body>
<%--   <div class="bar-menu">
     <jsp:include page="../member/membermenu.jsp"></jsp:include>
  </div> --%>
    <h1>게시글 작성</h1>

    <form:form modelAttribute="writeBoardVO" method="post" enctype="multipart/form-data">
      <div class="grid">
        <label for="subject">제목</label>
        <div>
        	<form:errors path="subject" element="div" cssClass="error" /> <!-- <div class="error"> subject의 유효성 검사 </div> -->
        	<input id="subject" type="text" name="subject" value="${writeBoardVO.subject}" />
		</div>
        
        <label for="file">첨부파일</label>
        <input type="file" type="file" name="file" />

        <label for="content">내용</label>
        <textarea id="content" name="content">${writeBoardVO.content}</textarea>

        <div class="btn-group">
          <div class="right-align">
            <button>저장</button>
          </div>
        </div>
      </div>
    </form:form>
  </body>
</html>

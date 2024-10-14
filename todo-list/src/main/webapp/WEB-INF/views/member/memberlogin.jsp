<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Todo 로그인</title>
<link rel="stylesheet" type="text/css" href="/css/login.css" />
</head>
<body>

	
    <form:form modelAttribute="loginMemberVO" method="post">
      <div class="group">
	      <h1>Todo 로그인</h1>
	      <div class="grid">
	        <label for="email">이메일</label>
	        <div>
	        	<input id="email" type="email" name="email" value="${loginMemberVO.email}" />
				<form:errors path="email" element="div" cssClass="error" />
				<c:if test="${not empty message}">
	      			<div class="error">${message}</div>
	      		</c:if>
			</div>
	        <label for="password">비밀번호</label>
	        <div>
	        	<input id="password" type="password" name="password" />
				<form:errors path="password" element="div" cssClass="error" />
			</div>
			
	        <div class="btn-group">
	          <div class="right-align">
	            <a class="regist" href="/member/regist">회원가입</a>
	          </div>
	          <div class="right-align">
	            <input type="submit" value="로그인" />
	          </div>
	        </div>
	      </div>
      </div>
    </form:form>

</body>
</html>
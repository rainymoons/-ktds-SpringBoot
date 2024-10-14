<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %> 
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Insert title here</title>
    <link rel="stylesheet" type="text/css" href="/css/common.css" />
  </head>
  <body>
    <h1>로그인</h1>
    <form:form modelAttribute="loginMemberVO" method="post">
      <div>
        <form:errors path="email" element="div" cssClass="error" />
        <form:errors path="password" element="div" cssClass="error" />
        <c:if test="${not empty message}">
	        <div class="error">${message}</div>
        </c:if>
      </div>

      <div class="grid grid-member-login">
        <label for="email">이메일</label>
        <input
          type="email"
          id="email"
          name="email"
          value="${loginMemberVO.email}"
        />

        <label for="password">비밀번호</label>
        <input
          type="password"
          id="password"
          name="password"
          value="${loginMemberVO.password}"
        />

        <div class="btn-group">
          <div class="right-align">
            <input type="submit" id="btn-regist" value="로그인" />
          </div>
        </div>
      </div>
    </form:form>
  </body>
</html>

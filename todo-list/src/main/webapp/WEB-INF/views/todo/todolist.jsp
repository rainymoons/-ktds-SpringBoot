<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Todo List</title>
	<link rel="stylesheet" type="text/css" href="/css/todo.css" />
</head>
<body>

	<div class="group">
	  <jsp:include page="../todo/todomenu.jsp"></jsp:include>
	  <div class="right-align">총 ${todoListVO.todoList.size()}건의 게시글이 등록되었습니다.</div>
      <table class="table">
        <thead>
          <tr>
            <th>번호</th>
            <th>완료?</th>
            <th>TODO Subject</th>
            <th>기한</th>
            <th></th>
            <th></th>
          </tr>
        </thead>
        <tbody>
        	<c:choose>
        		<c:when test="${not empty todoListVO.todoList}">
        			<c:forEach items="${todoListVO.todoList}" var="todo">
		        		<tr>
				            <td>${todo.id}</td>
				            <c:choose>
				            		<c:when test="${todo.isComplete == 1}">
				            			<td>DONE</td>
				            		</c:when>
				            		<c:otherwise>
				            			<td></td>
				            		</c:otherwise>
				            </c:choose>
						    <td>${todo.ctt}</td>
				            <td>${todo.deadline}</td>
				            <c:choose>
				            		<c:when test="${todo.isComplete == 0 && not empty sessionScope._LOGIN_USER_}">
				            			<td>
				            				<a href="/todo/complete${todo.id}">완료</a>
				            			</td>
				            		</c:when>
				            		<c:otherwise>
				            			<td></td>
				            		</c:otherwise>
				            </c:choose>
				            <td>
				            	<!-- <c:if test="${not empty sessionScope._LOGIN_USER_}"> -->
				            	<c:if test="{sessionScope._LOGIN_USER_.id eq todoVO.id}">
				            		<a href="/todo/delete${todo.id}">삭제</a>
				            	</c:if>
				            </td>
		          		</tr>
        			</c:forEach>
        		</c:when>
        		<c:otherwise>
        			<tr>
        				<td colspan="6">작성한 TODO 가 없습니다.</td>
        			</tr>
        		</c:otherwise>
        	</c:choose>
        </tbody>
      </table>
	  	<c:if test="${not empty sessionScope._LOGIN_USER_}">
			<div class="right-align">
		        <a href="/todo/write">새 아이템 추가</a>
		    </div>	  	
	  	</c:if>
    </div>
</body>
</html>
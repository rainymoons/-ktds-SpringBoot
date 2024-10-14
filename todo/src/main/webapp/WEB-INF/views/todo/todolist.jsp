<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>PlanBoard TODO LIST</title>
    <link rel="stylesheet" type="text/css" href="/css/todolist.css" />
  </head>
  <body>
    <div class="grid-container">
      <table class="todo-table">
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
<c:forEach items="${todolist}" var="todo">
    <tr>
        <td>${todo.id}</td>
        <td class="${todo.isdone == 1 ? 'DONE' : 'not-done'}">
            ${todo.isdone == 1 ? 'DONE' : '완료 안 됨'}
        </td>
        <td>${todo.subject}</td>
        <td>${todo.deadLine}</td>
        <td>
            <c:if test="${todo.isdone == 0}">
                <a href="/todo/isdone/${todo.id}" class="action-link">완료</a>
            </c:if>
        </td>
        <td>
            <a href="/todo/delete/${todo.id}" class="action-link">삭제</a>
        </td>
    </tr>
</c:forEach>
        </tbody>
      </table>
      <button class="add-item-btn" onclick="location.href='http://localhost:8080/todo/write'">새 아이템 추가</button>
    </div>
  </body>
</html>
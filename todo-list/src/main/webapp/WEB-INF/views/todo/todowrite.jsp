<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>새 아이템 추가</title>
	<link rel="stylesheet" type="text/css" href="/css/insert.css" />
</head>
<body>
	
    <form class="div-group" method="post">
      <h1>새 아이템 추가</h1>
      <div class="grid">
        <label for="ctt">제목</label>
        <input
          id="ctt"
          type="text"
          name="ctt"
          placeholder="제목 입력" />
        <label for="deadline">기한</label>
        <input
          id="deadline"
          type="date"
          name="deadline"
          placeholder="기한 날짜 선택" />
          
          <div class="btn-group">
        	<div class="right-align">
          		<button>저장</button>
        	</div>
      </div>
      </div>
    </form>
</body>
</html>
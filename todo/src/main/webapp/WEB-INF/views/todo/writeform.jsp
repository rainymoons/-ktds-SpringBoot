<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>write form</title>
    <link rel="stylesheet" type="text/css" href="styles/writeform.css">
</head>
<body>
<form method=post>
    <table class="form-table">
        <tr>
            <th class="form-header">제목</th>
            <input id="sub" type="text" placeholder="제목 입력" />
        </tr>
        <tr>
            <th class="form-header">기한</th>
            <input id="deadline" type="date" name="deadline" placeholder="기한 날짜 선택" />
        </tr>
    </table>

    <button type="submit" class="register-btn">등록</button>
    </form>
</body>
</html>
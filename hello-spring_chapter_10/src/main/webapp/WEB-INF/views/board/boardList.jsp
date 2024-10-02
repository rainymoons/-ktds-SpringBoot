<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- JSTL을 위한 Directive(taglib) 선언 (위치는 page Directive 아래)-->
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div class="grid">
		<div class= "right-align">
			총 ${boardListVO.boardCnt}건의 게시글이 검색되었습니다.
		<table class = "table">
			<thead>
				<tr>
					<th>번호</th>
					<th>제목</th>
					<th>이메일</th>
					<th>조회수</th>
					<th>등록일</th>
					<th>수정일</th>
				</tr>
			</thead>
			<tbody>
				<!-- 
					BoardListVO boardListVO = new BoardListVO();
					List<BoardVO> boardList = boardListVO.getBoardList();
					for (BoardVO board : boardList) 
					             = bar     = items   이걸 아래에 구현
					             forEach
				 -->
				 
				 <!--  
				 	${boardListVO.boardList} 가 비어있지 않다면,
				 	forEach를 통해 목록을 보여주고, 그렇지 않다면
				 	"게시글이 없습니다."를 보여준다.
				 	-> choose when otherwise
				  -->
			  	<c:choose>
			  		<c:when test="${not empty boardListVO.boardList}"> <!-- 리스트가 비어있다면(아닐경우 not) -->
			  			<c:forEach items="${boardListVO.boardList}" var="board">
							<tr>
								<td>${board.id}</td>
								<td>
									<a href="/board/view?id=${board.id}">
										${board.subject}
									 </a>
								</td>
								<td>${board.email}</td>
								<td>${board.viewCnt}</td>
								<td>${board.crtDt}</td>
								<td>${board.mdfyDt}</td>
							</tr>
						</c:forEach>	
			  		</c:when>
			  		<c:otherwise>
			  			<tr>
			  				<td colspan="6">게시글이 없습니다.</td>
			  			</tr>	
			  		</c:otherwise>
			  	</c:choose>
			</tbody>
		</table>
		<div class= "right-align">
			<a href="/board/write">게시글 등록</a>	
		</div>	
	</div>
</div>
</body>
</html>
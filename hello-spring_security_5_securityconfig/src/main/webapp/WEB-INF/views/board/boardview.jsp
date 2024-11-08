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
		
          <jsp:include page="../member/membermenu.jsp"></jsp:include>
        
    <h1>${boardVO.id}번 게시글 조회</h1>
    <div class="grid grid-view-board" data-board-id="${boardVO.id}"> <!-- 몇번 게시글을 보고 있는지 알고 싶어서 설정. 어디서? reply.js에서 -->
      <label for="subject">제목</label>
      <div>${boardVO.subject}</div>
      <label for="email">이메일</label>
      <div>${boardVO.memberVO.name}${boardVO.memberVO.email}</div>
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
      	<div class="reply-items">
<!--       		하위댓글이 상위댓글보다 조금 안쪽에 위치하도록 하기 위해 인라인 스타일 설정 레벨1이면 0rem -->
      		
      	</div>
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
    
    <template id="reply-template">
	    <div class="reply" data-reply-id="{replyId}" style="padding-left: {paddingLeft}rem">
	  		<div class="author">
	  			{authorName} ({authorEmail})
	  		</div>
	  		<div class="recommend-count">
	  			추천수: {recommendCount}
	  		</div>
	  		<div class="datetime">
	  			<span class="crtdt">등록: {crtDt}</span>
	  			<!-- 등록날짜 및 시간과 수정 날짜 및 시간이 다를 때에만 수정을 노출. -->
	  			<span class="mdfydt">수정: {mdfyDt}</span>
	  		</div>
	  		<!-- 내용물을 그대로 노출시킨다. -->
	  		<pre class="content">{content}</pre>
	  		<!-- 이 div는 로그인한 사용자가 작성한 댓글인 경우에만 노출. 내가 쓴것만 수정하고 삭제.-->
	  		<div class="my-reply">
	  			<span class="modify-reply">수정</span>
	  			<span class="delete-reply">삭제</span>
	  			<span class="re-reply">답변하기</span>
	  		</div>
	  		<!-- 다른 사람이 작성한 댓글일 경우에만 노출 -->
	  		<div class="other-reply">
	  			<span class="recommend-reply">추천하기</span>
	  			<span class="re-reply">답변하기</span>
 		</div>
	</div>
    </template>
  </body>
</html>

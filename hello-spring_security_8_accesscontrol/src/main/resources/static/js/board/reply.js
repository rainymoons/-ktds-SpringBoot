$().ready(function() {
	
    var email = $(".member-menu").data("email"); // 로그인한 사용자.
    var boardId = $(".grid-view-board").data("board-id");
	
    console.log("로그인 이메일: " + email);
    console.log("조회중인 게시글 번호: " + boardId);
	
	//
	loadReplies(boardId);
	
    $("#btn-save-reply").on("click", function() {
         createReply(boardId);	
	});
	
	// 글 작성 취소
	$("#btn-cancel-reply").on("click", function() {
		$(".txt-reply").val("");
		$(".txt-reply").removeData("modify-reply-id"); // data-modify-reply-id="" 를 지워줌.
		$(".txt-reply").removeData("parent-reply-id"); // 다음 댓글을 위해 parent-reply-id값을 지워준다.
	});
});

function loadReplies(boardId) {
	
	// 로그인한 사용자의 이메일을 받아온다.
	var email = $(".member-menu").data("email"); 
	
	// DOM을 모두 비워준다. -> 기존의 댓글은 모두 화면에서 제거한다.
	$(".reply-items").html("");
	
	// 파라미터는 없지만 비어있는 객체리터럴로 보내준다.
	$.get(`/board/reply/${boardId}`, {}, function(repliesResult) {
		
		var count = repliesResult.count; // 댓글 10개 존재시 여기
		var replies = repliesResult.replies; // 여기엔 배열. [ {..} , {..}, ... 10개]
		
		// 
		for (var i in replies) {
			// 댓글 목록에서 댓글 한개 뽑아옴. 템플릿에서 찾아서 넣어줘얗 ㅏㅁ.
			var reply = replies[i];
			
			var template = $("#reply-template").html();
			template = template.replace("{replyId}", reply.replyId) // 얘는 문자열.
							   .replace("{paddingLeft}", (reply.level - 1) * 1)
							   .replace("{authorName}", reply.memberVO.name)
							   .replace("{authorEmail}", reply.email)
							   .replace("{recommendCount}", reply.recommendCnt)
							   .replace("{crtDt}", reply.crtDt)
							   .replace("{mdfyDt}", reply.mdfyDt)
							   .replace("{content}", reply.content);
			//$(".reply-items").append($(template)); // html을 하나의 돔 형태로 만들어서 reply-items에 넣어준다.
			// 아래에서 append를 하고 있으니 여기서 apped를 할 필요가 없음.
		
		
			var replyDom = $(template);
			if (reply.crtDt === reply.mdfyDt) { // 수정된 적 없다. -> 수정날짜 감춤
				replyDom.find(".mdfydt").remove();
			}
			
			if (email === reply.email) { // 이건 내가 쓴 것. other-reply div를 없앤다.
				replyDom.find(".other-reply").remove();
				// 내것일때 수정하기 이벤트
				replyDom.find(".my-reply")
						.find(".modify-reply")
						.on("click", function(){
							modifyReply( $(this) );
						});
						
				replyDom.find(".my-reply")
						.find(".delete-reply")
						.on("click", function(){
							deleteReply($(this)); // delete-reply가 이벤트 발생시켰음. 얘는 span태그 delete-reply
							// 가장 인접한 부모에는 data-reply-id가 존재함.
						});
									
				replyDom.find(".my-reply")
						.find(".re-reply")
						.on("click", function(){
						 	reReply($(this));
						});
			} else {
				replyDom.find(".my-reply").remove();
				replyDom.find(".other-reply")
						.find(".recommend-reply")
						.on("click", function(){
							recommendReply( $(this) );
						});
						
				replyDom.find(".other-reply")
						.find(".re-reply")
						.on("click", function(){
							reReply($(this));
						});	
			}
			$(".reply-items").append(replyDom);
		}
	});
}

function createReply(boardId) {
	
	// 버튼을 누를때마다 error-div는 사라지고 에러가 생길 경우 다시 에러 메세지가출력된다.
	$(".write-reply").find("div.error").remove();
	
	// 댓글 등록 url
	var url = `/board/reply/${boardId}`;
	
	// 
	var modifyReplyId = $(".txt-reply").data("modify-reply-id"); 
	if (modifyReplyId) {
		// modifyReplyId가 존재한다면 수정을 위한 클릭
		url = `/board/reply/modify/${modifyReplyId}`;
	}
	
	var params = { 
		content: $(".txt-reply").val() 
	}
	// parentReplyId가 존재한다면 대댓글임을 의미함
	var parentReplyId = $(".txt-reply").data("parent-reply-id");
	
	if (parentReplyId) {
		// 대댓글인 경우를 의미함
		params.parentReplyId = parentReplyId;
	}
	
    $.post(url
		, params
		, function(createResult) {
		/**
		 * {"result": true}
		 */
		console.log(createResult)
		
		// result의 값이 true라면, 댓글의 목록을 갱신한다.
		if ( createResult.result) {
			// 올바르게 댓글을 등록했다면 텍스트 area를 초기화시킨다.
			$(".txt-reply").val("");
			$(".txt-reply").removeData("modify-reply-id"); // data-modify-reply-id="" 를 지워줌.
			$(".txt-reply").removeData("parent-reply-id"); // 다음 댓글을 위해 parent-reply-id값을 지워준다.
			loadReplies(boardId)
		} else if(createResult.error) {
			alert(createResult.error);
		}
		else {
			// 에러가 존재하는 케이스
			// 키가 무엇인지 모름. 키는 result로 받아와야 하는데, 무엇이 들어올지 모르기 때문.
			
			var errorDiv = $("<div class='error'</div>");
			var errorUl = $("<ul></ul>");
			errorDiv.append(errorUl);
			
			// key 변수에 createResult에 있는 키의 값이 반복 할당 된다.
			// JSON(Object Literal) 을 반복
			for (var key in createResult) {
				
				// createResult[key]를 반복.
				for (var i in createResult[key]) { // 메세지 하나씩 나옴.
					var errorLi = $("<li></li>");
					errorLi.text(createResult[key][i]);
					errorUl.append(errorLi);
				}
				
				// key = content, createResult.key = createResult에서 key변수에 들어있는 값을 가져와라.
				//console.log(key, createResult[key]);
			}
			$(".txt-reply").before(errorDiv);
		}
	});
}

function modifyReply(clickedModifyButton) {
	var replyId = clickedModifyButton.closest(".reply") // 수정하고 싶은 댓글 찾고
									 .data("reply-id");

	var replyContent = clickedModifyButton.closest(".reply") // 댓글의 내용
					   					  .find(".content")
					   					  .text();
	// <textarea class="txt-reply" data-modify-reply-id=""> 아래 코드가 이렇게 만들어줌.
	$(".txt-reply").val(replyContent);
	// 답변하기 선택 후 수정하기를 눌렀을 때 수정하기가 온전히 작동하도록 답변하기의 id 값을 지운다.
	$(".txt-reply").removeData("parent-reply-id");
	// 나는 지금 이 게시글을 수정하려고 한다 라는 정보
	$(".txt-reply").data("modify-reply-id", replyId); // data-modify-reply-id 이게 있으면 지금 수정을 한다는 것.
	$(".txt-reply").focus();
	
	
}

function deleteReply(clickedDeleteButton) {
	
	var replyId = clickedDeleteButton.closest(".reply") // span 클래스가 속한 div의 부모에는 data-reply-id가 있음. 거기서 id정보를 가져온다.
									 .data("reply-id"); 
	
	// 삭제를 클릭하면 삭제할거냐고 묻고 확인을 누르면 삭제
	if (confirm("정말 삭제할까요?")) {
		$.get(`/board/reply/delete/${replyId}`, {}, function(deleteResult) {
			if (deleteResult.result) { // 삭제 결과가 true이면
				var boardId = $(".grid-view-board").data("board-id");
				loadReplies(boardId);
			} else if (deleteResult.error) {
				alert(deleteResult.error);
			}
		});
	}
}

function recommendReply(clickedRecommendButton) {
	var replyId = clickedRecommendButton.closest(".reply")
										 .data("reply-id");
										 
	$.get(`/board/reply/recommend/${replyId}`, {},
		function(recommendResult) {
			if (recommendResult.result) {
				var boardId = $(".grid-view-board").data("board-id");
				loadReplies(boardId);
			} else if(recommendResult.error) {
				alert(recommendResult.error); // 에러가 존재하면 alert을 띄워라.
			}
		});
}

function reReply(clickedReReplyButton) {
	// 내가 선택한 댓글의 번호. 여기에다가 대댓글을 달고싶다.
	var replyId = clickedReReplyButton.closest(".reply")
									  .data("reply-id");

	$(".txt-reply").data("parent-reply-id", replyId); //parent-reply-id 여기가 reply인지 modify인지에 따라 수정인지대댓글 등록인지 나뉨.
	$(".txt-reply").removeData("modify-reply-id"); // 이걸 지워야 대댓글만 온전히 작동함.
	// 답변하기 -> 수정하기(수정 내용물이 생김) -> 답변하기 시 content가 사라지면 좋겠다. 커서도 생기고(focus)
	$(".txt-reply").val("");
	$(".txt-reply").focus();
}
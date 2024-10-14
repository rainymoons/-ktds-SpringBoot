$().ready(function() {
	
	var email = $(".member-menu").data("email");
	var boardId = $(".grid-view-board").data("board-id");
	
	console.log("로그인 이메일: " + email);
	console.log("조회중인 게시글 번호: " + boardId);
	
	$("#btn-save-reply").on("click", function() {
		createReply(boardId);	
	});
});

function loadReplies(boardId) {
	
}

function createReply(boardId) {
	$.post(`/board/reply/${boardId}`, {content: $(".txt-reply").val()}
		, function(createResult) {
		/**
		 * {"result": true}
		 */
		console.log(createResult)
		
		// result의 값이 true라면, 댓글의 목록을 갱신한다.
		if ( createResult.result) {
			loadReplies(boardId)
		}
	});
}

function modifyReply(replyId) {
	
}

function deleteReply(replyId) {
	
}

function recommendReply(replyId) {
	
}


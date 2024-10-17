$().ready(function() {
	
	// 그룹 아이디에게 메세지를 보낸다.
	$(".send-chat-message-btn").on("click", function() {
		
		var groupId = $(".chat-dialog").data("group-id");
		var chatMessage = $(".chat-message").val();
		
		$(".chat-message").val("");// 초기화
		
		sendChat(groupId, chatMessage); // 그룹아이디에 메세지를 보낸다.
	});
	
	// 종료
	$(".close-dialog-btn").on("click", function() {
		
		var groupId = $(".chat-dialog").data("group-id");
		$(".chat-dialog").removeData("group-id");
		$(".chat-dialog")[0].close();
		
		quitChat(groupId);
	});
});
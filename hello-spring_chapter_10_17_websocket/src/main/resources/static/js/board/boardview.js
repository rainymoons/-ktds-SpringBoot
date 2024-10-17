$().ready(function() {
	$(".chat-btn").on("click", function() {
		// 상대방에게 채팅을 요청
		var to = $(this).data("you-email"); // 상대방
		
		var me = $(".member-menu").data("email");
		
		requestChat(to, me); // to에게 me가 대화를 요청한다.
	});
});
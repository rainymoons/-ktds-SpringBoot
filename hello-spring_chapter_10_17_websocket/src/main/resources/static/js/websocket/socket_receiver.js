
function receiveHandler(socket) {

		if (socket) { // socket_sender의 socket을 가져오고 싶다.
		
		// 웹 소켓 서버에서 메세지가 전달 되었을 때 동작되는 이벤트 = onmessage
		socket.onmessage = function(message) {
			console.log(message);
			
			var receiveData = message.data; // JSON 타입.
			
			// JSON -> Object Literal로 변경
			var receiveMessage = JSON.parse(receiveData);
			
			if (receiveMessage.action === "LOGIN") { // 액션이 로그인이면
				alert(receiveMessage.message); // 메세지를 띄운다.
			}
			
			// 알림 서비스
			else if (receiveMessage.action === "REPLY_ALARM") {
				if (confirm(receiveMessage.message)) {
					location.href = receiveMessage.url;
				}
			// 채팅 서비스
			} else if(receiveMessage.action === "REQUEST_CHAT") {
				if (confirm(receiveMessage.message)) {
					// 1. dialog를 띄운다.
					$(".chat-dialog").data("group-id", receiveMessage.groupId)
					$(".chat-dialog")[0].show(); // dialog가 뜸.
					
					// 2. 요청을 수락하는 메세지를 대화 요청자에게 보낸다.
					sendResponseForChat(receiveMessage.to, receiveMessage.from, receiveMessage.groupId, true); // to는 '나', 수락하면 true 
					
					
				}
				else {
					// 요청을 거부하는 메세지를 대화 요청자에게 보낸다.(취소)
					sendResponseForChat(receiveMessage.to, receiveMessage.from, receiveMessage.groupId, false); // to는 '나', 수락하면 true
				}
				
			} else if(receiveMessage.action === "RESPONSE_CHAT") {
				if(receiveMessage.isConnect === "true") {
					$(".chat-dialog").data("group-id", receiveMessage.groupId); // receiveMessage에 groupId 전달.
					$(".chat-dialog")[0].show();
				} else {
					alert("상대방이 대화를 거절했습니다.");
				}
			}
			else if (receiveMessage.action === "SEND_CHAT") {
				$(".chat-dialog")[0].show();
				$(".chat-dialog").data("group-id", receiveMessage.groupId);
				
				var chat = $("<div class=`chat`></div>");
				chat.append(`<div>${receiveMessage.sender} : ${receiveMessage.chatMessage}</div>`);
				
				$(".chat-history").append(chat);
				
			}
			else if (receiveMessage.action === "QUIT_CHAT") {
				$(".chat-dialog")[0].show();
				$(".chat-dialog").data("group-id", receiveMessage.groupId);
				
				var chat = $("<div class=`chat`></div>");
				chat.append(`<div>${receiveMessage.sender} : 퇴장했습니다.</div>`);
				
				$(".chat-history").append(chat);
			}
		} 
	}
}

// socket_receiver에서 socket을 사용하기 위한 작업
var socket = undefined;

$().ready(function () {
	
	// membermenu에서 사용자의 email을 땡겨옴.
	var userEmail = $(".member-menu").data("email");
	
	// 브라우저 렌더링이 끝나면 웹소켓에 연결한다.
	// socket 변수 = 소켓과 연결되어있는 파이프.
	socket = new SockJS("/ws");
	
	// 소켓 연결이 정상적으로 이루어졌을 때의 동작을 작성.
	socket.onopen = function() { // 소켓이 연결되었으면 이 function을 수행해라.
		
		receiveHandler(socket);
		
		// OOO가 접속했습니다! 라는 alert이 뜨도록 설정.
		var sendMessage = {
			email: userEmail,
			action: "LOGIN",
			message: userEmail + "님이 접속했습니다!"
		};
		
		// 연결된 이후에 웹소켓 서버로 메세지를 전송한다.
		socket.send(JSON.stringify(sendMessage)); // 객체 리터럴을 JSON형태로 만들어서 서버로 메세지 전송
	};
});

function sendAlarmWriteReply(boardId) {
	
	var userEmail = $(".member-menu").data("email");
	
	var sendMessage = { // 전송에 필요한 데이터를 모은다.
		email: userEmail,
		action: "REPLY_ALARM",
		boardId: `${boardId}`, // 변환하면서 double로 바뀌기 때문에 문자로 바꿔서 전송.
		url: "/board/view?id=" + boardId,
		message: "새로운 댓글이 등록되었습니다! 확인하러 가시겠습니까?"
	};
	
	socket.send(JSON.stringify(sendMessage));
}

function requestChat(to, me) {
	var sendMessage = {
		email: me,
		action: "REQUEST_CHAT",
		to: to,
		message: me + " 회원이 대화를 요청했습니다. 수락하시겠습니까?"
	};
	socket.send(JSON.stringify(sendMessage));
}

function sendResponseForChat(from, to, groupId, isConnect) {
	var sendMessage = {
		email: from, // 왜 갑자기 from이 됬냐?
		action: "RESPONSE_CHAT",
		to: to,
		groupId: groupId,
		isConnect: `${isConnect}` // boolean을 String으로 바꿔서 보내준다. isConnect 에러 해
	};
	socket.send(JSON.stringify(sendMessage));
}

function sendChat(groupId, chatMessage) {
	
	var userEmail = $(".member-menu").data("email");
	
	var sendMessage = {
			sender: userEmail, 
			action: "SEND_CHAT",
			groupId: groupId,
			chatMessage: chatMessage
		};
		socket.send(JSON.stringify(sendMessage));
}


function quitChat(groupId) {

	var userEmail = $(".member-menu").data("email");

	var sendMessage = {
			sender: userEmail, 
			action: "QUIT_CHAT",
			groupId: groupId,
		};
		socket.send(JSON.stringify(sendMessage));
}

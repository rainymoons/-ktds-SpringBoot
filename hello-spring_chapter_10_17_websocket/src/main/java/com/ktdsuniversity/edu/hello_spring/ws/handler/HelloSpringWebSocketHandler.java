package com.ktdsuniversity.edu.hello_spring.ws.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;
import com.ktdsuniversity.edu.hello_spring.bbs.dao.BoardDao;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardVO;

/**
 * /ws로 통신하는 모든 요청들을 처리한다.
 * 특정 사용자가 전송한 메시지를 /ws에 접속한 모든 클라이언트에게 전달하는 역할을 수행한다.
 * 일종의 브로커 역할.
 */
@Component
public class HelloSpringWebSocketHandler extends TextWebSocketHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(HelloSpringWebSocketHandler.class); 

	// 댓글을 누가 썼는지 알기위해 dao 직접 접근.
	@Autowired
	private BoardDao boardDao;
	
	
	/**
	 * 웹소켓에 연결되어있는 사용자(WebSocketSession)를 관리하는 변수.
	 * Key: 접속된 사용자의 이메일(PK)
	 * Value: WebSocket Session 정보.
	 * 누군가가 접속하면 connectedSessionMap에 추가하는 것.
	 */
	private Map<String, WebSocketSession> connectedSessionMap;
	
	/**
	 * 다 대 다 통신을 위한 세션 그룹
	 * 참여자 모두 그룹에서 나갔다면, 해당 그룹아이디를 그룹에서 제거한다.
	 * 
	 * key: 그룹 아이디
	 * Value: 그룹 참여자 이메일(PK) 목록
	 */
	private Map<String, List<String>> sessionGroupMap;
	
	/**
	 * JSON <--> JavaObject 변환
	 */
	private Gson gson;
	
	public HelloSpringWebSocketHandler() {
		this.connectedSessionMap = new HashMap<>();
		this.sessionGroupMap = new HashMap<>();
		
		this.gson = new Gson();
	}
	
	/**
	 * handleTextMessage의 역할
	 *  - socket에 연결된 사용자가 /ws로 메세지를 보내면 handleTextMessage가 수신한다.
	 *    원하는 사용자에게 메세지를 전송해준다.
	 *    
	 *    @param session : 텍스트 데이터를 보낸 접속자의 정보
	 *    @param message : 서버로 보내진 텍스트 데이터
	 *    즉, session이 message를 보냈다.
	 *    모두에게 메세지를 보내는건 지금은 못함. 어떻게 함? HelloSpringWebSocketHandler가 접속한 사람들의 모든 정보를 가지고 있어야 함.
	 */
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// message.getPayload() -> 사용자가 보낸 텍스트 메시지를 꺼낸다.
		String payload = message.getPayload(); // 얘가 지금 JSON 형태 -> GSON으로 바꾼다.
		
		// payload에서 email을 추출.
		// payload에서 action을 추출
		// payload에서 message를 추출.
		// --> 이걸 하기 위해서 payload(JSON형태)를 Map으로 변환한다. (JSON 형태의 데이터를 Map으로 변환)
		// --> GSON 라이브러리 필요함.
		Map<String, String> payloadMap = this.gson.fromJson(payload, Map.class);
		String action = payloadMap.get("action");
		String email = payloadMap.get("email");
		String receiveMessage = payloadMap.get("message");
		
		// 첫 로그인일때만 알림 메세지를 보낸다.
		if (action.equals("LOGIN")) {
			// 액션이 로그인이면 세션에 접속을 함.
			
			// 로그인 했던 사람이면(값이 true) 맵에 email이 등록되어 있을 것.
			boolean loggedSession = this.connectedSessionMap.containsKey(email); 
			this.connectedSessionMap.put(email, session); // session이 사용자임.
			
			if (!loggedSession) {
				// 모든 사용자에게 전송할 메세지. JSON으로 변환해서 보내야 함.
				Map<String, String> textMessageMap = new HashMap<>();
				textMessageMap.put("sender", email);
				textMessageMap.put("action", action);
				textMessageMap.put("message", receiveMessage);
				
				// 메서드로 분리해서 불러옴.
				sendToOtherSessions(textMessageMap, session); // session이 '나'를 의미. -> 나를 제외한 모든 세션에게 보내라.
			}
		
		// 알림
		} else if (action.equals("REPLY_ALARM")) {
			// 글을 누가 썼는지 알고 싶음.
			int boardId = Integer.parseInt(payloadMap.get("boardId"));
			String url = payloadMap.get("url");
			
			BoardVO boardVO = this.boardDao.selectOneBoard(boardId);
			String boardWriter = boardVO.getEmail(); // 이 boardWriter에게만 메세지를 보내면 된다.
			
			Map<String, String> textMessageMap = new HashMap<>();
			textMessageMap.put("action", "REPLY_ALARM");
			textMessageMap.put("message", receiveMessage);
			textMessageMap.put("url", url);
			
			sendToOneSession(textMessageMap, boardWriter);
		
		// 채팅
		} else if (action.equals("REQUEST_CHAT")) {
			
			Map<String, String> textMessageMap = new HashMap<>();
			textMessageMap.put("action", "REQUEST_CHAT");
			textMessageMap.put("message", receiveMessage);
			textMessageMap.put("from", payloadMap.get("email"));
			textMessageMap.put("to", payloadMap.get("to")); // email이 to한테 채팅을 신청함. 신청에 대한 확인을 누르면 한 공간에 밀어넣으려고 추가함.
			
			String groupId = UUID.randomUUID().toString();
			textMessageMap.put("groupId", groupId);
			
			sendToOneSession(textMessageMap, payloadMap.get("to")); // to에게 receiveMessage를 보낸다.
		
		// 채팅 응답
		} else if(action.equals("RESPONSE_CHAT")) {
			
			Map<String, String> textMessageMap = new HashMap<>();
			textMessageMap.put("action", "RESPONSE_CHAT");
			textMessageMap.put("isConnect", payloadMap.get("isConnect"));
			textMessageMap.put("from", payloadMap.get("email"));
			textMessageMap.put("to", payloadMap.get("to")); 
			
			// isConnect가 true일 때, 두 사용자를 한 그룹으로 묶어준다.
			String isConnect = payloadMap.get("isConnect");
			if (isConnect.equals("true")) { // 초대를 수락하면
				
				String groupId = payloadMap.get("groupId");
				
				textMessageMap.put("groupId", groupId);// 수락하면 groupId가 포함되어있을 것이고, 거절하면 없음. 그때 groupId를 넣어서 보낸다.
				// 대화가 시작하면 groupId를 가지고 대화가 진행된다.
				
				if(this.sessionGroupMap.containsKey(groupId)) {
					// 이미 대화가 진행중인데 한명을 더 초대하는 상황.
					this.sessionGroupMap.get(groupId).add(payloadMap.get("email"));
				}
				else {
					List<String> groupMember = new ArrayList<>();
					groupMember.add(payloadMap.get("email"));
					groupMember.add(payloadMap.get("to"));
					
					this.sessionGroupMap.put(groupId, groupMember); // 세션 그룹에 넣어준다.
				}
			}
			sendToOneSession(textMessageMap, payloadMap.get("to")); 
		}
		 else if(action.equals("SEND_CHAT")) {
			Map<String, String> textMessageMap = new HashMap<>();
			textMessageMap.put("action", "SEND_CHAT");
			textMessageMap.put("sender", payloadMap.get("sender"));
			textMessageMap.put("groupId", payloadMap.get("groupId"));
			textMessageMap.put("chatMessage", payloadMap.get("chatMessage"));
			
			sendToGroupMembers(payloadMap.get("groupId"), textMessageMap); // groupId에 있는 모든 사람들에게 챗을 보내라.
			
		}
			
		else if(action.equals("QUIT_CHAT")) {
			
			String sender = payloadMap.get("sender");
			String groupId = payloadMap.get("groupId");
			
			Map<String, String> textMessageMap = new HashMap<>();
			textMessageMap.put("action", "QUIT_CHAT");
			textMessageMap.put("sender", sender);
			textMessageMap.put("groupId", groupId);
			
			this.sessionGroupMap.get(payloadMap.get("groupId"))
									.removeIf(groupEmail -> groupEmail.equals(sender));
			
			if (this.sessionGroupMap.get(groupId).size() == 0) {
				this.sessionGroupMap.remove(groupId);
			}
			
			sendToGroupMembers(payloadMap.get("groupId"), textMessageMap); // groupId에 있는 모든 사람들에게 챗을 보내라.
		}
	}
	
	/**
	 * 전송자를 제외한 모든 세션에게 메세지를 전달한다.
	 * @param textMessageMap : 세션에 보낼 메세지
	 * @param session : 전송자
	 */
	private void sendToOtherSessions(Map<String, String> textMessageMap, WebSocketSession session) {
		// 맵을 JSON으로 바꿔서 TextMessage 객체로 만든다.
		TextMessage textMessage = new TextMessage(this.gson.toJson(textMessageMap));
		
		// 모든 사용자에게 접속 알림을 전송한다.
		// 접속한 사용자들은 connectedSessionMap에 있음.
		this.connectedSessionMap.entrySet()
								.stream()
								.filter(entry -> entry.getValue() != session) // 각 세션에서 나(전송자)를 빼는것. 나빼고 메세지 보내야하니까.
								.forEach(entry -> {
									try {
										WebSocketSession each = entry.getValue();
										if (each.isOpen()) { // 연결이 끊어지지 않았다면, 연결이 된것만 가지고 와라.
											/*
											 * 세션 호출 직렬화
											 * 한 세션이 동시에 여러가지를 주고 받게 되었을 경우 실패가 발생하고,
											 * 연결을 강제로 끊어버리게 된다.
											 * 순차로 실행될 수 있도록 직렬화 한다.
											 */
											synchronized(each) {
												each.sendMessage(textMessage);
											}
										}
									} catch (IOException e) {
										logger.error(e.getMessage(), e);
									}
								});
	}
	
	private void sendToOneSession(Map<String, String> textMessageMap, String receiverEmail) {
		
		// 1. receiverEmail 사용자가 로그인을 한 상태인가?
		if (!this.connectedSessionMap.containsKey(receiverEmail) ) { // 연결된 세션에 이메일이 Key로 존재하지 않는다면.
			// receiverEmail 사용자가 로그인을 하지 않은 상태
			// TODO 일괄 전송 준비
			return;
		}
		// 위에서 했던 것 반복
		TextMessage textMessage = new TextMessage(this.gson.toJson(textMessageMap));
		WebSocketSession session = this.connectedSessionMap.get(receiverEmail);
		if (session.isOpen()) {
			try {
				synchronized (session) {
					session.sendMessage(textMessage);
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		
	}
	
	private void sendToGroupMembers(String groupId, Map<String, String> textMessageMap) {
		
		TextMessage textMessage = new TextMessage(this.gson.toJson(textMessageMap));
		
		this.sessionGroupMap.get(groupId)
							.stream()
							.map(email -> this.connectedSessionMap.get(email)) // session이 나오게 됨.
							.forEach(session -> { // 그룹에 있는 사람들에게만 보낸다.
								if (session.isOpen()) {
									try {
										synchronized (session) {
											session.sendMessage(textMessage);
										}
									} catch (IOException e) {
										logger.error(e.getMessage(), e);
									}
								}
							});
	}
}

package com.ssafy.jara.common.chating;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketHandler extends TextWebSocketHandler {
	// TextWebSocketHandler는 handleTextMessage를 실행시킴
	// 메시지 타입에 따라서 handleBinaryMessage 또는 handleTextMessage 실행

	HashMap<String, WebSocketSession> sessionMap = new HashMap<>(); // 웹소켓 세션을 담아둘 맵
	
	// 메시지 발송 시 실행
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		String msg = message.getPayload(); // message.getPayload()로 메시지 전송받음
		JSONObject obj = JsonToObjectParser(msg); // JSON형태로 전송된 메시지 파싱
		
		for(String key : sessionMap.keySet()) {
			WebSocketSession wss = sessionMap.get(key);
			try {
				wss.sendMessage(new TextMessage(obj.toJSONString()));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// 웹소켓 연결 시 동작
	@SuppressWarnings("unchecked")
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
		sessionMap.put(session.getId(), session);
		
		JSONObject obj = new JSONObject();
		obj.put("type", "getId"); // 발신메시지의 타입
		obj.put("sessionId", session.getId()); // 생성된 세션 Id
		session.sendMessage(new TextMessage(obj.toJSONString()));
	}
	
	// 웹소켓 종료 시 동작
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessionMap.remove(session.getId());
		super.afterConnectionClosed(session, status);
	}
	
	// JSON 파싱
	private static JSONObject JsonToObjectParser(String jsonStr) {
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		
		try {
			obj = (JSONObject) parser.parse(jsonStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return obj;
	}
}

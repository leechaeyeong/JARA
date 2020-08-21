package com.ssafy.jara.common.chating;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {

	@MessageMapping("/receive")
	@SendTo("/send")
	public Socket SocketHandler(Socket socket) {
		String userName = socket.getUserName();
		String content = socket.getContent();
		int user_id = socket.getUser_id();
		
		Socket result = new Socket(userName, content, user_id);
		
		return result;
	}
}

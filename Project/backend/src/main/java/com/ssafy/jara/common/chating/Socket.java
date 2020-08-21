package com.ssafy.jara.common.chating;

public class Socket {
	private String userName;	// 사용자
	private String content;		// 채팅 내용
	private int user_id;

	public Socket() {}

	public Socket(String userName, String content, int user_id) {
		super();
		this.userName = userName;
		this.content = content;
		this.user_id = user_id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	
	@Override
	public String toString() {
		return "Socket [userName=" + userName + ", content=" + content + ", user_id=" + user_id + "]";
	}
}

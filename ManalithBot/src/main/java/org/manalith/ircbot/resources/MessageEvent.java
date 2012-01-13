package org.manalith.ircbot.resources;

/*
 * Created on 2005. 7. 26
 */

public class MessageEvent {
	private final String channel;
	private final String sender;
	private final String login;
	private final String hostname;

	private String message;
	private boolean executed; // 실행 완료 여부

	// This is for PrivateMessage
	public MessageEvent(String sender, String login, String hostname,
			String message) {
		this.channel = null;
		this.sender = sender;
		this.login = login;
		this.hostname = hostname;
		this.message = message;
	}

	public MessageEvent(String channel, String sender, String login,
			String hostname, String message) {
		this.channel = channel;
		this.sender = sender;
		this.login = login;
		this.hostname = hostname;
		this.message = message;
	}

	public String getChannel() {
		return channel;
	}

	public String getHostname() {
		return hostname;
	}

	public String getLogin() {
		return login;
	}

	public String getMessage() {
		return message;
	}

	public String getSender() {
		return sender;
	}

	public boolean isExecuted() {
		return executed;
	}

	public void setExecuted(boolean executed) {
		this.executed = executed;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
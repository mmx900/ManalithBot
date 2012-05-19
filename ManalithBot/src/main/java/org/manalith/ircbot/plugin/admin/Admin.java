package org.manalith.ircbot.plugin.admin;

import org.pircbotx.User;

public class Admin {
	private String login;
	private String realName;
	private String hostmask;
	private String server;

	public Admin(User user) {
		this.login = user.getLogin();
		this.realName = user.getRealName();
		this.hostmask = user.getHostmask();
		this.server = user.getServer();
	}

	public boolean equals(User user) {
		//TODO 더 강력한 유저 식별 방법이 필요함
		return user.getLogin().equals(login)
				&& user.getRealName().equals(realName)
				&& user.getHostmask().equals(hostmask)
				&& user.getServer().equals(server);
	}
}

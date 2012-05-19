package org.manalith.ircbot.plugin.admin;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.springframework.stereotype.Component;

@Component
public class AdminPlugin extends AbstractBotPlugin {
	private List<Admin> admins = new ArrayList<Admin>();
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		if (password.equals("CHANGE_ME")) {
			throw new IllegalArgumentException("기본값을 사용할 수 없습니다.");
		}

		this.password = password;
	}

	@Override
	public String getName() {
		return "AUTH";
	}

	@Override
	public String getCommands() {
		return null;
	}

	@Override
	public String getHelp() {
		return "인증과 관리를 지원합니다.";
	}

	@Override
	public void onMessage(MessageEvent event) {
		String message = event.getMessage();
		Channel channel = event.getChannel();

		if (isAdmin(event.getUser()) && message.equals("!@")) {
			int i = 0;

			// 모든 사용자에게 옵을 준다
			for (User user : channel.getUsers()) {
				if (!channel.getOps().contains(user)
						&& !channel.getSuperOps().contains(user)
						&& !channel.getOwners().contains(user)) {
					channel.op(user);
					i++;
				}
			}

			if (i == 0) {
				event.getBot().sendLoggedMessage(channel.getName(),
						"모든 사용자가 옵을 가지고 있습니다.");
			}
		}
	}

	private boolean isAdmin(User user) {
		for (Admin admin : admins) {
			if (admin.equals(user)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onPrivateMessage(MessageEvent event) {
		ManalithBot bot = event.getBot();
		String message = event.getMessage();
		String sender = event.getUser().getNick();

		if (StringUtils.left(message, 3).equals("pw ")) {
			if (StringUtils.substring(message, 3).equals(password)) {
				admins.add(new Admin(event.getUser()));
				bot.sendLoggedMessage(sender, "인증되었습니다.");
			} else {
				bot.sendLoggedMessage(sender, "비밀번호가 잘못되었습니다.");
			}

			event.setExecuted(true);
		}
	}
}

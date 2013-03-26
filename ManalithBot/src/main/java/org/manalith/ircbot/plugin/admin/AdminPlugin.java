package org.manalith.ircbot.plugin.admin;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

		if (message.equals("!나가")) {
			if (event.getChannel().isOp(event.getUser())) {
				event.getBot().partChannel(event.getChannel());
				event.setExecuted(true);
			} else {
				event.respond("옵을 가진 사용자만 실행할 수 있습니다.");
			}
		} else if (message.equals("!@")) {
			if (event.getChannel().isOp(event.getUser())) {
				if (!event.getChannel().isOp(event.getBot().getUserBot())) {
					event.respond("봇에게 옵이 필요합니다.");
				}

				int i = 0;

				// 모든 사용자에게 옵을 준다
				Channel channel = event.getChannel();
				for (User user : channel.getUsers()) {
					if (!channel.getOps().contains(user)
							&& !channel.getSuperOps().contains(user)
							&& !channel.getOwners().contains(user)) {
						channel.op(user);
						i++;
					}
				}

				if (i == 0) {
					event.respond("모든 사용자가 옵을 가지고 있습니다.");
				}

				event.setExecuted(true);
			} else {
				event.respond("옵을 가진 사용자만 실행할 수 있습니다.");
			}
		}

		if (isAdmin(event.getUser())) {
			if (message.equals("!uptime")) {
				RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
				long upTime = bean.getUptime();
				event.respond(String.format("Up Time = %d (ms)", upTime));
			} else if (message.equals("!quit")) {
				// TODO 보다 안전한 종료가 필요
				// TODO 멀티봇 대응 필요
				// TODO 재시작 기능이 필요
				event.getBot().quitServer();
				System.exit(-1);
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
		String message = event.getMessage();

		if (StringUtils.left(message, 3).equals("pw ")) {
			if (StringUtils.substring(message, 3).equals(password)) {
				admins.add(new Admin(event.getUser()));
				event.respond("인증되었습니다.");
			} else {
				event.respond("비밀번호가 잘못되었습니다.");
			}
		}
	}
}

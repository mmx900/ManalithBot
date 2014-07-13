package org.manalith.ircbot.plugin.admin;

import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.springframework.stereotype.Component;

@Component
public class BasicPlugin extends SimplePlugin {

	@Override
	public String getName() {
		return "기본 명령어";
	}

	@BotCommand
	public String echo(String message) {
		return message;
	}
}

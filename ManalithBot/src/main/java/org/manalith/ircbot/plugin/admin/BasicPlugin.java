package org.manalith.ircbot.plugin.admin;

import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.springframework.stereotype.Component;

@Component
public class BasicPlugin extends SimplePlugin {

	@Override
	public String getName() {
		return "echo";
	}

	@Override
	public String getHelp() {
		return "설  명: 인자를 그대로 반환합니다. 사용법: !echo [명령]";
	}

	@BotCommand
	public String echo(String message) {
		return message;
	}
}

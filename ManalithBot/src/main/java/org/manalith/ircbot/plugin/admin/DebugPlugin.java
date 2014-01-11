package org.manalith.ircbot.plugin.admin;

import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;
import org.manalith.ircbot.annotation.NotNull;
import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.springframework.stereotype.Component;

@Component
public class DebugPlugin extends SimplePlugin {

	private static final String[] COMMANDS = { "!len" };

	@Override
	public String getName() {
		return "디버거";
	}

	@Override
	public String getCommands() {
		return StringUtils.join(COMMANDS, ",");
	}

	@BotCommand
	public String len(@NotNull String values) {
		char first = StringUtils.left(values, 1).charAt(0);
		char last = StringUtils.right(values, 1).charAt(0);
		return String.format("%d char / %d bytes ('%s' %s ~ '%s' %s)",
				values.length(),
				values.getBytes(Charset.forName("UTF-8")).length, first,
				Character.getName(first), last, Character.getName(last));
	}
}

package org.manalith.ircbot.plugin.admin;

import java.nio.charset.Charset;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

@Component
public class DebugPlugin extends AbstractBotPlugin {
	private static final String[] COMMANDS = { "!len" };

	@Override
	public String getName() {
		return "디버거";
	}

	@Override
	public String getCommands() {
		return StringUtils.join(COMMANDS, ",");
	}

	@Override
	public void onMessage(MessageEvent event) {
		String[] segs = event.getMessageSegments();
		if (segs.length > 1 && ArrayUtils.contains(COMMANDS, segs[0])) {
			String values = StringUtils.substringAfter(event.getMessage(), " ");
			char first = StringUtils.left(values, 1).charAt(0);
			char last = StringUtils.right(values, 1).charAt(0);
			event.respond(String.format(
					"%d char / %d bytes ('%s' %s ~ '%s' %s)", values.length(),
					values.getBytes(Charset.forName("UTF-8")).length, first,
					Character.getName(first), last, Character.getName(last)));
		}
	}
}

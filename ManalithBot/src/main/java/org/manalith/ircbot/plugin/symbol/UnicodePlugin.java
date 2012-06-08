package org.manalith.ircbot.plugin.symbol;

import org.apache.commons.lang3.StringUtils;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

@Component
public class UnicodePlugin extends AbstractBotPlugin {

	@Override
	public String getName() {
		return "유니코드 변환 플러그인";
	}

	@Override
	public String getCommands() {
		return "!유니코드 [변환할 문자 혹은 코드]";
	}

	@Override
	public void onMessage(MessageEvent event) {
		String[] segments = StringUtils.split(event.getMessage(), " ");
		if (segments.length == 2
				&& (segments[0].equals("!유니코드") || segments[0]
						.equals("!unicode"))) {
			StringBuilder sb = new StringBuilder();
			char[] chars = segments[1].toCharArray();
			for (Character ch : chars) {
				sb.append("U+" + Integer.toHexString(ch | 0x10000).substring(1));
			}
			if (chars.length == 1) {
				sb.append(" - ");
				sb.append(Character.getName(chars[0]));
			}
			event.respond(sb.toString());
		}
	}

}

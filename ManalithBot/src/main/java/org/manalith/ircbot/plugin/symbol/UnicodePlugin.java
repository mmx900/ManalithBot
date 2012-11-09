package org.manalith.ircbot.plugin.symbol;

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
		String[] segments = event.getMessageSegments();
		if (segments.length == 2
				&& (segments[0].equals("!유니코드") || segments[0]
						.equals("!unicode"))) {
			String text = segments[1];
			try {
				if (text.startsWith("\\u") || text.startsWith("U+")) {
					char ch = (char) Integer.parseInt(text.substring(2), 16);
					event.respond(ch + " - " + Character.getName(ch));
				} else {
					char[] chars = text.toCharArray();
					StringBuilder sb = new StringBuilder();
					for (char ch : chars) {
						sb.append("U+"
								+ Integer.toHexString(ch | 0x10000)
										.substring(1));
					}
					if (chars.length == 1) {
						sb.append(" - ");
						sb.append(Character.getName(chars[0]));
					}
					event.respond(sb.toString());
				}
			} catch (NumberFormatException e) {
				event.respond("잘못된 유니코드 문자열입니다.");
			}
		}
	}

}

package org.manalith.ircbot.plugin.symbol;

import org.apache.commons.lang3.StringUtils;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

@Component
public class SymbolPlugin extends AbstractBotPlugin {

	@Override
	public String getName() {
		return "심볼 찾기 플러그인";
	}

	@Override
	public String getCommands() {
		return "!심볼";
	}

	public String getHelp() {
		return "설  명: 심볼을 텍스트로 보여줍니다, 사용법: !심볼 [심볼(그림문자)]";
	}

	@Override
	public void onMessage(MessageEvent event) {
		String[] segments = StringUtils.split(event.getMessage(), " ");
		if (segments.length == 2 && segments[0].equals("!심볼")) {
			switch (segments[1]) {
			case "->":
				event.respond("→");
				break;
			case "<-":
				event.respond("←");
				break;
			case "v":
				event.respond("✌");
				break;
			case "*":
				event.respond("•");
				break;
			case ":-)":
				event.respond("☺");
				break;
			case ":-(":
				event.respond("☹");
				break;
			case "x":
				event.respond("✖");
				break;
			case "...":
				event.respond("…");
				break;
			}
		}
	}

}

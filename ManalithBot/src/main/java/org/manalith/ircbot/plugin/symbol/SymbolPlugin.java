package org.manalith.ircbot.plugin.symbol;

import org.manalith.ircbot.annotation.Option;
import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.springframework.stereotype.Component;

@Component
public class SymbolPlugin extends SimplePlugin {

	@Override
	public String getName() {
		return "심볼 찾기";
	}

	@Override
	public String getDescription() {
		return "심볼을 텍스트로 보여줍니다.";
	}

	@BotCommand("심볼")
	public String symbol(
			@Option(name = "심볼", help = "변환할 심볼") String str) {
		switch (str) {
		case "->":
			return "→";
		case "<-":
			return "←";
		case "v":
			return "✌";
		case "*":
			return "•";
		case ":-)":
			return "☺";
		case ":-(":
			return "☹";
		case "x":
			return "✖";
		case "...":
			return "…";
		}

		return "해당하는 문자가 없습니다.";
	}

}

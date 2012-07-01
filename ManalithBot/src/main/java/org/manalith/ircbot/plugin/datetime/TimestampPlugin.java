package org.manalith.ircbot.plugin.datetime;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

@Component
public class TimestampPlugin extends AbstractBotPlugin {
	@Override
	public String getName() {
		return "유닉스 타임스탬프 변환";
	}

	@Override
	public String getCommands() {
		return "!timestamp";
	}

	public String getHelp() {
		return "설  명: 유닉스 타임스탬프 문자열을 ISO 날짜형식으로 변환합니다, 사용법: !timestamp [timestamp]";
	}

	@BotCommand(value = { "!timestamp" }, minimumArguments = 1)
	public String convertTimeStamp(MessageEvent event, String... args) {
		return convertTimeStamp(Long.parseLong(args[0]));
	}

	public String convertTimeStamp(long timestamp) {
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss (z Z)");
		return format.format(new Date(timestamp * 1000));
	}

}

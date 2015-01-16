package org.manalith.ircbot.plugin.datetime;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.manalith.ircbot.annotation.Option;
import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.springframework.stereotype.Component;

@Component
public class TimestampPlugin extends SimplePlugin {

	@Override
	public String getName() {
		return "유닉스 타임스탬프 변환";
	}

	@Override
	public String getDescription() {
		return "유닉스 타임스탬프 문자열을 ISO 날짜형식으로 변환합니다.";
	}

	@BotCommand("timestamp")
	public String convertTimeStamp(
			@Option(name = "타임스탬프", help = "변경할 타임스태프") String timestamp) {
		return convertTimeStamp(Long.parseLong(timestamp));
	}

	public String convertTimeStamp(long timestamp) {
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss (z Z)");
		return format.format(new Date(timestamp * 1000));
	}

}

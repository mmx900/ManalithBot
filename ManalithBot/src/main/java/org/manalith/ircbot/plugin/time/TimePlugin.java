package org.manalith.ircbot.plugin.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

@Component
public class TimePlugin extends SimplePlugin {

	private final String[] commands = { "!시간", "!time", "!월요일", "!금요일", "!토요일",
			"!일요일", "!주말" };
	private final SimpleDateFormat format = new SimpleDateFormat(
			"yyyy년 MM월 dd일 HH시 mm분 ss초");

	@Override
	public String getName() {
		return "시간계산";
	}

	@Override
	public String getCommands() {
		return StringUtils.join(commands, '|');
	}

	@Override
	public void onMessage(MessageEvent event) {
		String cmd = event.getMessage();

		if (ArrayUtils.contains(commands, cmd)) {
			Date now = new Date();

			switch (cmd) {
			case "!시간":
			case "!time":
				event.respond(format.format(now));
				break;
			case "!월요일": {
				String str = getDiff(now, Calendar.MONDAY);
				event.respond(String.format("월요일 생성까지 %s 남았습니다.", str));
				break;
			}
			case "!금요일": {
				String str = getDiff(now, Calendar.FRIDAY);
				event.respond(String.format("금요일 생성까지 %s 남았습니다.", str));
				break;
			}
			case "!토요일": {
				String str = getDiff(now, Calendar.SATURDAY);
				event.respond(String.format("토요일 생성까지 %s 남았습니다.", str));
				break;
			}
			case "!일요일": {
				String str = getDiff(now, Calendar.SUNDAY);
				event.respond(String.format("일요일 생성까지 %s 남았습니다.", str));
				break;
			}
			case "!주말": {
				String str = getDiff(now, Calendar.SATURDAY);
				event.respond(String.format("주말 생성까지 %s 남았습니다.", str));
				break;
			}
			}
		}
	}

	public Date getNextDate(Date today, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		int dow = cal.get(Calendar.DAY_OF_WEEK);

		do {
			cal.add(Calendar.DAY_OF_YEAR, 1);
			dow = cal.get(Calendar.DAY_OF_WEEK);
		} while (dow != day);

		return cal.getTime();
	}

	// http://stackoverflow.com/questions/635935/how-can-i-calculate-a-time-span-in-java-and-format-the-output
	private String getDiff(Date now, int day) {
		long diffInSeconds = (getNextDate(now, day).getTime() - now.getTime()) / 1000;
		long diff[] = new long[] { 0, 0, 0, 0 };
		/* sec */diff[3] = (diffInSeconds >= 60 ? diffInSeconds % 60
				: diffInSeconds);
		/* min */diff[2] = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60
				: diffInSeconds;
		/* hours */diff[1] = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24
				: diffInSeconds;
		/* days */diff[0] = (diffInSeconds = (diffInSeconds / 24));

		return String.format("%d일 %d시 %d분 %d초", diff[0], diff[1], diff[2],
				diff[3]);
	}
}

package org.manalith.ircbot.plugin.time;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

@Component
public class TimePlugin extends SimplePlugin {

	private final String[] commands = { "!시간", "!time", "!월요일", "!금요일", "!토요일",
			"!일요일", "!주말" };
	private final DateTimeFormatter dateFormatter = DateTimeFormat
			.forPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
	private final PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
			.appendYears().appendSuffix("년 ").appendMonths().appendSuffix("월 ")
			.appendWeeks().appendSuffix("주 ").appendDays().appendSuffix("일 ")
			.appendHours().appendSuffix("시 ").appendMinutes()
			.appendSuffix("분 ").appendSeconds().appendSuffix("초")
			.printZeroNever().toFormatter();

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
			DateTime now = new DateTime();

			switch (cmd) {
			case "!시간":
			case "!time":
				event.respond(dateFormatter.print(now));
				break;
			case "!월요일": {
				String str = getPeriod(now, DateTimeConstants.MONDAY);
				event.respond(String.format("다음 월요일 생성까지 %s 남았습니다.", str));
				break;
			}
			case "!금요일": {
				String str = getPeriod(now, DateTimeConstants.FRIDAY);
				event.respond(String.format("다음 금요일 생성까지 %s 남았습니다.", str));
				break;
			}
			case "!토요일": {
				String str = getPeriod(now, DateTimeConstants.SATURDAY);
				event.respond(String.format("다음 토요일 생성까지 %s 남았습니다.", str));
				break;
			}
			case "!일요일": {
				String str = getPeriod(now, DateTimeConstants.SUNDAY);
				event.respond(String.format("다음 일요일 생성까지 %s 남았습니다.", str));
				break;
			}
			case "!주말": {
				String str = getPeriod(now, DateTimeConstants.SATURDAY);
				event.respond(String.format("다음 주말 생성까지 %s 남았습니다.", str));
				break;
			}
			}
		}
	}

	@BotCommand({ "dday" })
	public String getPeriod(String year, String monthOfYear, String dayOfMonth) {
		DateTime target = new DateTime(Integer.parseInt(year),
				Integer.parseInt(monthOfYear), Integer.parseInt(dayOfMonth), 0,
				0);
		Period period = new Period(new DateTime(), target);
		return periodFormatter.print(period);
	}

	public DateTime getNextDate(DateTime source, int dayOfWeek) {
		DateTime date = new DateTime(source.getYear(), source.getMonthOfYear(),
				source.getDayOfMonth(), 0, 0);
		if (date.getDayOfWeek() < dayOfWeek) {
			return date.withDayOfWeek(dayOfWeek);
		} else {
			return date.plusWeeks(1).withDayOfWeek(dayOfWeek);
		}
	}

	private String getPeriod(DateTime now, int dayOfWeek) {
		Period period = new Period(now, getNextDate(now, dayOfWeek));

		return periodFormatter.print(period);
	}
}

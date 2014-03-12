package org.manalith.ircbot.plugin.google;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.springframework.stereotype.Component;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;

@Component
public class CalendarPlugin extends SimplePlugin {

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd a hh:mm");

	private String appName;
	private String accountId;
	private String p12path;
	private String calendarId;

	private final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

	@Override
	public String getName() {
		return "구글 달력 플러그인";
	}

	@BotCommand({ "다음일정" })
	public String getCalendar() throws Exception {

		GoogleCredential credentials = new GoogleCredential.Builder()
				.setTransport(GoogleNetHttpTransport.newTrustedTransport())
				.setJsonFactory(jsonFactory)
				.setServiceAccountId(accountId)
				.setServiceAccountScopes(
						Arrays.asList(CalendarScopes.CALENDAR_READONLY))
				.setServiceAccountPrivateKeyFromP12File(new File(p12path))
				.build();

		Calendar client = new Calendar.Builder(
				GoogleNetHttpTransport.newTrustedTransport(), jsonFactory,
				credentials).setApplicationName(appName).build();

		List<Event> events = client.events().list(calendarId).setMaxResults(1)
				.setTimeMin(new DateTime(new Date())).setOrderBy("startTime")
				.setSingleEvents(true).execute().getItems();

		if (!events.isEmpty()) {
			Event event = events.get(0);
			return String.format(
					"%s : %s %s - %s",
					FORMAT.format(new Date(event.getStart().getDateTime()
							.getValue())),
					StringUtils.defaultIfBlank(event.getSummary(), ""),
					StringUtils.defaultIfBlank(event.getDescription(), ""),
					event.getCreator().getDisplayName()).replace("\n", "");
		}

		return "표시할 내용이 없습니다.";

	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getP12path() {
		return p12path;
	}

	public void setP12path(String p12path) {
		this.p12path = p12path;
	}

	public String getCalendarId() {
		return calendarId;
	}

	public void setCalendarId(String calendarId) {
		this.calendarId = calendarId;
	}
}

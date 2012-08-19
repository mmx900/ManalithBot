package org.manalith.ircbot.plugin.onoffmix;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

@Component
/**
 * 온오프믹스 API를 이용해 정보를 가져오는 플러그인.
 * @see http://developer.onoffmix.com/doku.php
 *
 */
public class OnOffMixPlugin extends AbstractBotPlugin {
	private Logger logger = Logger.getLogger(getClass());
	private String apiKey;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	@Override
	public String getName() {
		return "온오프믹스 플러그인";
	}

	@Override
	public String getCommands() {
		return "!onoffmix [조회할 이벤트ID]";
	}

	@Override
	public String getHelp() {
		return "설  명: 온오프믹스 행사를 조회합니다., 사용법: !onoffmix [조회할 이벤트ID]";
	}

	@BotCommand(value = { "!onoffmix" }, minimumArguments = 1)
	public String getEventInfo(String... args) {
		int eventIdx = NumberUtils.toInt(StringUtils.join(args));

		if (eventIdx == 0)
			return getHelp();

		try {
			return String.format("%s (%s) http://onoffmix.com/event/%d",
					getEventInfo(eventIdx), getEventUserInfo(eventIdx),
					eventIdx);
		} catch (IllegalArgumentException e) {
			return e.getMessage();
		}
	}

	/**
	 * @see http://developer.onoffmix.com/doku.php?id=이벤트_참석자_목록보기
	 * @param eventIdx
	 * @return
	 */
	private String getEventInfo(int eventIdx) {
		try (InputStream input = new URL(String.format(
				"http://api.onoffmix.com/event/%d?apikey=%s&output=JSON",
				eventIdx, apiKey)).openStream()) {
			Reader reader = new InputStreamReader(input, "UTF-8");
			OnOffMixEventInfoResponse res = new Gson().fromJson(reader,
					OnOffMixEventInfoResponse.class);

			if (StringUtils.isNotEmpty(res.error.message)) {
				throw new IllegalArgumentException(res.error.message);
			} else {
				return String.format("%s : %s", res.event.title,
						res.event.abstractText);
			}
		} catch (IOException e) {
			logger.warn(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * @see http://developer.onoffmix.com/doku.php?id=이벤트_내용보기
	 * @param eventIdx
	 * @return
	 */
	private String getEventUserInfo(int eventIdx) {
		try (InputStream input = new URL(
				String.format(
						"http://api.onoffmix.com/rsvp/event?apikey=%s&eventIdx=%d&output=JSON",
						apiKey, eventIdx)).openStream()) {
			Reader reader = new InputStreamReader(input, "UTF-8");
			OnOffMixUserInfoResponse res = new Gson().fromJson(reader,
					OnOffMixUserInfoResponse.class);

			if (StringUtils.isNotEmpty(res.error.message)) {
				throw new IllegalArgumentException(res.error.message);
			} else {
				return String.format("참여 %d명, 대기 %d명",
						res.groupList.get(0).authList.size(),
						res.groupList.get(0).standbyList.size());
			}
		} catch (IOException e) {
			logger.warn(e.getMessage(), e);
		}

		return null;
	}

	public static class OnOffMixEventInfoResponse {
		public OnOffMixError error;
		public OnOffMixEvent event;
	}

	public static class OnOffMixEvent {
		public int idx;
		public String title;

		@SerializedName("abstract")
		public String abstractText;
	}

	public static class OnOffMixUserInfoResponse {
		public OnOffMixError error;
		public List<OnOffMixGroup> groupList;
	}

	public static class OnOffMixError {
		public int code;
		public String message;
	}

	public static class OnOffMixGroup {
		public int idx;
		public String name;
		public List<OnOffMixMember> authList;
		public List<OnOffMixMember> standbyList;
	}

	public static class OnOffMixMember {
		public String name;
		public String email;
	}
}

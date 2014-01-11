package org.manalith.ircbot.plugin.onoffmix;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.manalith.ircbot.annotation.Description;
import org.manalith.ircbot.annotation.NotNull;
import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Component
/**
 * 온오프믹스 API를 이용해 정보를 가져오는 플러그인.
 * @see http://developer.onoffmix.com/doku.php
 *
 */
public class OnOffMixPlugin extends SimplePlugin {

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
		return "온오프믹스";
	}

	@Override
	public String getCommands() {
		return "!onoffmix [조회할 이벤트ID]";
	}

	@Override
	public String getHelp() {
		return "설  명: 온오프믹스 행사를 조회합니다., 사용법: !onoffmix [조회할 이벤트ID]";
	}

	@BotCommand("!onoffmix")
	public String getEventInfo(
			@Description("조회할 이벤트 ID") @NotNull String eventId) {
		int eventIdx = NumberUtils.toInt(eventId);

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
		RestTemplate rest = new RestTemplate();
		rest.getMessageConverters().add(
				new MappingJackson2HttpMessageConverter());

		try {
			OnOffMixEventInfoResponse res = rest
					.getForObject(
							"http://api.onoffmix.com/event/{eventIdx}?apikey={apiKey}&output=JSON",
							OnOffMixEventInfoResponse.class, eventIdx, apiKey);
			if (StringUtils.isNotEmpty(res.error.message)) {
				throw new IllegalArgumentException(res.error.message);
			} else {
				return String.format("%s : %s", res.event.title,
						res.event.abstractText);
			}
		} catch (RestClientException e) {
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
		RestTemplate rest = new RestTemplate();
		rest.getMessageConverters().add(
				new MappingJackson2HttpMessageConverter());

		try {
			OnOffMixUserInfoResponse res = rest
					.getForObject(
							"http://api.onoffmix.com/rsvp/event?apikey={apiKey}&eventIdx={eventIdx}&output=JSON",
							OnOffMixUserInfoResponse.class, apiKey, eventIdx);

			if (StringUtils.isNotEmpty(res.error.message)) {
				throw new IllegalArgumentException(res.error.message);
			} else {
				return String.format("참여 %d명, 대기 %d명",
						res.groupList.get(0).authList.size(),
						res.groupList.get(0).standbyList.size());
			}
		} catch (RestClientException e) {
			logger.warn(e.getMessage(), e);
		}

		return null;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class OnOffMixEventInfoResponse {
		public OnOffMixError error;
		public OnOffMixEvent event;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class OnOffMixEvent {
		public int idx;
		public String title;

		@JsonProperty("abstract")
		public String abstractText;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class OnOffMixUserInfoResponse {
		public OnOffMixError error;
		public List<OnOffMixGroup> groupList;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class OnOffMixError {
		public int code;
		public String message;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class OnOffMixGroup {
		public int idx;
		public String name;
		public List<OnOffMixMember> authList;
		public List<OnOffMixMember> standbyList;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class OnOffMixMember {
		public String name;
		public String email;
	}
}

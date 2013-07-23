package org.manalith.ircbot.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtils {
	private static final Pattern PATTERN_URI = Pattern
			.compile(".*(https?://\\S+).*");

	/**
	 * 메시지 본문에서 http 혹은 https로 시작하는 URI를 추출해 반환한다. URI가 없을 경우 null을 반환한다.
	 * 
	 * @param msg
	 *            URI를 추출해낼 원본 메시지
	 * @return 추출해낸 메시지 혹은 null
	 */
	public static String findUri(String msg) {
		if (!msg.contains("http"))
			return null;

		Matcher matcher = PATTERN_URI.matcher(msg);

		if (!matcher.matches())
			return null;

		return matcher.group(1);
	}
}

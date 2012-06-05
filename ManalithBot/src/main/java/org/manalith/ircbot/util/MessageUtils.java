package org.manalith.ircbot.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

public class MessageUtils {
	private static Logger logger = Logger.getLogger(MessageUtils.class);

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

		String URI_REGEX = ".*(https?://\\S+).*";
		Pattern pattern = Pattern.compile(URI_REGEX);
		Matcher matcher = pattern.matcher(msg);

		if (!matcher.matches())
			return null;

		return matcher.group(1);
	}

	/**
	 * 주어진 URI에 접근하여 얻은 JSON 텍스트를 객체로 변환해 반환한다.
	 * 
	 * @param uri
	 *            JSON 텍스트의 URI
	 * @param typeOfT
	 *            JSON 객체의 값을 대입할 객체의 형식
	 * @return JSON 객체의 값을 대입한 객체
	 */
	@SuppressWarnings("unchecked")
	public static <T> T loadFromJson(URI uri, Type typeOfT) {
		try {
			HttpGet get = new HttpGet(uri);

			DefaultHttpClient httpclient = new DefaultHttpClient();
			Gson gson = new Gson();
			return (T) gson.fromJson(
					new InputStreamReader(httpclient.execute(get).getEntity()
							.getContent()), typeOfT);
		} catch (IOException e) {
			logger.error(e);
		}

		return null;
	}
}

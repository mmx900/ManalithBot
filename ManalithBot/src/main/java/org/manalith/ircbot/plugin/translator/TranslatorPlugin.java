package org.manalith.ircbot.plugin.translator;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.manalith.ircbot.annotation.Description;
import org.manalith.ircbot.annotation.NotNull;
import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TranslatorPlugin extends SimplePlugin {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private String clientSecret;

	@Override
	public String getName() {
		return "Bing Translator";
	}

	@BotCommand("번역")
	public String translate(@NotNull @Description("ko|en...") String to,
			@NotNull @Description("메시지") String message) {
		final String url = "https://api.datamarket.azure.com/Bing/MicrosoftTranslator/v1/Translate?Text='%s'&To='%s'";
		String login = "USER_ID_IGNORED:" + clientSecret;
		String base64login = new String(Base64.encodeBase64(login.getBytes()));

		try {
			Document doc = Jsoup.connect(String.format(url, message, to))
					.header("Authorization", "Basic " + base64login)
					.ignoreContentType(true).get();
			logger.debug("response", doc);
			Elements elem = doc.select("d|text[m:type=Edm.String]");
			return elem.text();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return "번역할 내용이 없습니다.";
	}

	/**
	 * @return the clientSecret
	 */
	public String getClientSecret() {
		return clientSecret;
	}

	/**
	 * @param clientSecret
	 *            the clientSecret to set
	 */
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
}

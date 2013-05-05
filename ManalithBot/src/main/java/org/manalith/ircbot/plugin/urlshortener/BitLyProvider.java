package org.manalith.ircbot.plugin.urlshortener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class BitLyProvider implements UrlShortenerProvider {
	private Logger logger = Logger.getLogger(getClass());
	private String apiLogin;
	private String apiKey;

	public String shorten(String url) {
		RestTemplate rest = new RestTemplate();
		rest.getMessageConverters().add(
				new MappingJacksonHttpMessageConverter());

		try {
			ShortenApiResponse data = rest
					.getForObject(
							"http://api.bitly.com/v3/shorten?login={apiLogin}&apiKey={apiKey}&longUrl={longUrl}",
							ShortenApiResponse.class, apiLogin, apiKey,
							URLEncoder.encode(url, "UTF-8"));

			return data.data.url;
		} catch (UnsupportedEncodingException | RestClientException e) {
			logger.warn(e.getMessage(), e);
		}

		return null;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiLogin() {
		return apiLogin;
	}

	public void setApiLogin(String apiLogin) {
		this.apiLogin = apiLogin;
	}

	/**
	 * bit.ly url shortener api 응답 모델. 참고 :
	 * http://dev.bitly.com/links.html#v3_shorten
	 * 
	 * @author setzer
	 * 
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ShortenApiResponse {
		public Data data;
		public String status_code;
		public String status_txt;

		@JsonIgnoreProperties(ignoreUnknown = true)
		public static class Data {
			public String global_hash;
			public String hash;
			public String long_url;
			public String new_hash;
			public String url;
		}
	}
}

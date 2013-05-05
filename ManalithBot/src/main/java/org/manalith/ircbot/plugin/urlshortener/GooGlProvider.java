package org.manalith.ircbot.plugin.urlshortener;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class GooGlProvider implements UrlShortenerProvider {
	private Logger logger = Logger.getLogger(getClass());
	private String apiKey;

	@Override
	public String shorten(String url) {
		ShortenApiRequest req = new ShortenApiRequest();
		req.longUrl = url;

		RestTemplate rest = new RestTemplate();
		rest.getMessageConverters().add(
				new MappingJacksonHttpMessageConverter());

		try {
			ShortenApiResponse data = rest.postForObject(
					"https://www.googleapis.com/urlshortener/v1/url?key={key}",
					req, ShortenApiResponse.class, apiKey);

			return data.id;
		} catch (RestClientException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * goo.gl url shortener api 요청 모델. 참고 :
	 * https://developers.google.com/url-shortener/v1/getting_started
	 * 
	 * @author setzer
	 * 
	 */
	public static class ShortenApiRequest {
		public String longUrl;
	}

	/**
	 * goo.gl url shortener api 응답 모델. 참고 :
	 * https://developers.google.com/url-shortener/v1/getting_started
	 * 
	 * @author setzer
	 * 
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ShortenApiResponse {
		public String kind;
		public String id;
		public String longUrl;
	}

}

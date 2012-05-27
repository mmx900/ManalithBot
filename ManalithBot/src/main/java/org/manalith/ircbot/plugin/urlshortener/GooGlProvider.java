package org.manalith.ircbot.plugin.urlshortener;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class GooGlProvider implements UrlShortenerProvider {
	private Logger logger = Logger.getLogger(getClass());
	private String apiKey;

	@Override
	public String shorten(String url) {
		ShortenApiRequest requestModel = new ShortenApiRequest();
		requestModel.setLongUrl(url);

		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(
				String.format(
						"https://www.googleapis.com/urlshortener/v1/url?key=%s",
						apiKey));

		try {
			StringEntity stringEntity = new StringEntity(
					new Gson().toJson(requestModel));
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);

			HttpResponse response = client.execute(httpPost);

			Reader reader = new InputStreamReader(response.getEntity()
					.getContent(), "UTF-8");
			ShortenApiResponse data = new Gson().fromJson(reader,
					ShortenApiResponse.class);

			return data.getId();
		} catch (IOException e) {
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
	public class ShortenApiRequest {
		private String longUrl;

		public String getLongUrl() {
			return longUrl;
		}

		public void setLongUrl(String longUrl) {
			this.longUrl = longUrl;
		}
	}

	/**
	 * goo.gl url shortener api 응답 모델. 참고 :
	 * https://developers.google.com/url-shortener/v1/getting_started
	 * 
	 * @author setzer
	 * 
	 */
	public class ShortenApiResponse {
		private String kind;
		private String id;
		private String longUrl;

		public String getKind() {
			return kind;
		}

		public void setKind(String kind) {
			this.kind = kind;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getLongUrl() {
			return longUrl;
		}

		public void setLongUrl(String longUrl) {
			this.longUrl = longUrl;
		}
	}

}

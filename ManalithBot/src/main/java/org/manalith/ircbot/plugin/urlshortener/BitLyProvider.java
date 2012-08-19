package org.manalith.ircbot.plugin.urlshortener;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class BitLyProvider implements UrlShortenerProvider {
	private Logger logger = Logger.getLogger(getClass());
	private String apiLogin;
	private String apiKey;

	public String shorten(String url) {
		try (InputStream input = new URL(
				String.format(
						"http://api.bitly.com/v3/shorten?login=%s&apiKey=%s&longUrl=%s",
						apiLogin, apiKey, URLEncoder.encode(url, "UTF-8")))
				.openStream()) {
			Reader reader = new InputStreamReader(input, "UTF-8");
			ShortenApiResponse data = new Gson().fromJson(reader,
					ShortenApiResponse.class);

			return data.getData().getUrl();
		} catch (IOException e) {
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
	public class ShortenApiResponse {

		private Data data;
		private String status_code;
		private String status_txt;

		public Data getData() {
			return data;
		}

		public void setData(Data data) {
			this.data = data;
		}

		public String getStatus_code() {
			return status_code;
		}

		public void setStatus_code(String status_code) {
			this.status_code = status_code;
		}

		public String getStatus_txt() {
			return status_txt;
		}

		public void setStatus_txt(String status_txt) {
			this.status_txt = status_txt;
		}

		public class Data {
			private String global_hash;
			private String hash;
			private String long_url;
			private String new_hash;
			private String url;

			public String getGlobal_hash() {
				return global_hash;
			}

			public void setGlobal_hash(String global_hash) {
				this.global_hash = global_hash;
			}

			public String getHash() {
				return hash;
			}

			public void setHash(String hash) {
				this.hash = hash;
			}

			public String getLong_url() {
				return long_url;
			}

			public void setLong_url(String long_url) {
				this.long_url = long_url;
			}

			public String getNew_hash() {
				return new_hash;
			}

			public void setNew_hash(String new_hash) {
				this.new_hash = new_hash;
			}

			public String getUrl() {
				return url;
			}

			public void setUrl(String url) {
				this.url = url;
			}
		}
	}
}

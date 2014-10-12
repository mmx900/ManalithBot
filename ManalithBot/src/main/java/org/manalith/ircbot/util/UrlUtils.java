package org.manalith.ircbot.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlUtils {
	private static Logger logger = LoggerFactory.getLogger(UrlUtils.class);

	public static String encode(String url) {
		try {
			return URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// impossible
			logger.error("encode error", e);
			return null;
		}
	}

	public static String decode(String url) {
		try {
			return URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// impossible
			logger.error("decode error", e);
			return null;
		}
	}

	/**
	 * SSL 요청시 호스트를 따지지 않고 접속하도록 설정한다.
	 * https://github.com/mmx900/ManalithBot/issues/100 참고. TODO optional
	 * 
	 * @throws Exception
	 */
	public static void setTrustAllHosts() throws Exception {
		// 인증서를 검증하지 않는다.
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] certs,
					String authType) {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] certs,
					String authType) {
			}
		} };

		SSLContext context = SSLContext.getInstance("SSL");
		context.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(context
				.getSocketFactory());

		// 호스트명을 검증하지 않는다.
		HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
	}
}

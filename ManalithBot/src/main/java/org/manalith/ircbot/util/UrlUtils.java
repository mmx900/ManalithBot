package org.manalith.ircbot.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

public class UrlUtils {
	private static Logger logger = Logger.getLogger(UrlUtils.class);

	public static String encode(String url) {
		try {
			return URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// impossible
			logger.error(e);
			return null;
		}
	}

	public static String decode(String url) {
		try {
			return URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// impossible
			logger.error(e);
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
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs,
					String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs,
					String authType) {
			}
		} };

		SSLContext context = SSLContext.getInstance("SSL");
		context.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(context
				.getSocketFactory());

		// 호스트명을 검증하지 않는다.
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}
}

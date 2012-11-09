package org.manalith.ircbot.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

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
}

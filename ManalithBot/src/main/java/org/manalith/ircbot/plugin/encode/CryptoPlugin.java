package org.manalith.ircbot.plugin.encode;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.springframework.stereotype.Component;

@Component
public class CryptoPlugin extends SimplePlugin {

	@Override
	public String getName() {
		return "인코더";
	}

	@BotCommand
	public String sha1(String input) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
		return Hex.encodeHexString(messageDigest.digest(input.getBytes(Charset
				.forName("UTF-8"))));
	}

	@BotCommand
	public String md5(String input) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		return Hex.encodeHexString(messageDigest.digest(input.getBytes(Charset
				.forName("UTF-8"))));
	}
}

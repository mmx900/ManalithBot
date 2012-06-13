package org.manalith.ircbot.console;

import jline.console.ConsoleReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.manalith.ircbot.remote.RemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Launcher {
	@Autowired
	private RemoteService remoteService;

	public static void main(String[] args) throws Exception {
		// 옵션 반영
		Options options = new Options();
		options.addOption("c", true, "config file");

		CommandLineParser parser = new PosixParser();
		CommandLine cmd = parser.parse(options, args);

		String configFile = "config.xml";
		String configFileOptionArg = cmd.getOptionValue("c");
		if (StringUtils.isNotBlank(configFileOptionArg)) {
			configFile = configFileOptionArg;
		}

		// 설정 초기화
		ApplicationContext context = new FileSystemXmlApplicationContext(
				configFile);

		// FIXME 인증 수행
		UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
				"admin", "admin");
		SecurityContextHolder.getContext().setAuthentication(userToken);
		Launcher launcher = context.getBean(Launcher.class);

		ConsoleReader reader = new ConsoleReader();
		String line;
		while (!StringUtils
				.equals((line = reader.readLine("prompt> ")), "exit")) {
			String[] strs = StringUtils.split(line, " ");
			launcher.sendMessage(strs[0], strs[1]);

			// System.out.println(line);
		}

	}

	private void sendMessage(String target, String message) {
		try {
			remoteService.sendMessage(target, message);
		} catch (BadCredentialsException e) {
			System.out.println("인증이 올바르지 않습니다.");
			Logger.getLogger(Launcher.class).error(e);
		}

	}

}

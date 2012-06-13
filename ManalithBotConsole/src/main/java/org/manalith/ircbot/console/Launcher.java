package org.manalith.ircbot.console;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang3.StringUtils;
import org.manalith.ircbot.remote.RemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
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

		Launcher launcher = context.getBean(Launcher.class);
		launcher.test();
	}

	private void test() {
		// FIXME method stub
		remoteService.sendMessage("#setzer", "test");
	}

}

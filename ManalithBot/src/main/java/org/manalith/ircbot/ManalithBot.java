/*
 	org.manalith.ircbot/ManalithBot.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2005, 2011  Ki-Beom, Kim
 	Copyright (C) 2011, 2012  Seong-ho, Cho <darkcircle.0426@gmail.com>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.manalith.ircbot;

import java.nio.charset.Charset;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.manalith.ircbot.plugin.EventDispatcher;
import org.manalith.ircbot.plugin.relay.RelayPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.manalith.ircbot.util.AppContextUtils;
import org.manalith.ircbot.util.UrlUtils;
import org.pircbotx.PircBotXUtf8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ManalithBot extends PircBotXUtf8 {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private Configuration configuration;

	@Autowired
	private EventDispatcher eventDispatcher;

	public ManalithBot(org.pircbotx.Configuration<ManalithBot> configuration) {
		super(configuration);
	}

	public static ManalithBot getInstance() {
		return AppContextUtils.getApplicationContext().getBean(
				ManalithBot.class);
	}

	public void invokeMessageEvent(MessageEvent event) {
		eventDispatcher.dispatchMessageEvent(event);
	}

	public void sendMessage(String target, String message) {
		sendMessage(target, message, true);
	}

	public void sendMessage(String target, String message, boolean needRelay) {
		sendIRC().message(target, message);

		logger.info("MESSAGE(LOCAL) : {} / {}", target, message);

		if (needRelay && RelayPlugin.RELAY_BOT != null)
			RelayPlugin.RELAY_BOT.sendIRC().message(target, message);
	}

	public Configuration getManalithBotConfiguration() {
		return configuration;
	}

	public static void main(String[] args) throws Exception {
		// 인코딩 검사
		if (!Charset.defaultCharset().toString().equals("UTF-8")) {
			System.out.println("-Dfile.encoding=UTF-8 옵션으로 실행시켜 주세요.");
			return;
		}

		// 옵션 반영
		Options options = new Options();
		options.addOption("c", true, "config file");

		CommandLineParser parser = new PosixParser();
		CommandLine cmd = parser.parse(options, args);

		String configFile = cmd.getOptionValue("c", "config.xml");

		// SSL/HTTPS 설정
		UrlUtils.setTrustAllHosts();

		// 설정 초기화
		ApplicationContext context = new FileSystemXmlApplicationContext(
				configFile);
		AppContextUtils.setApplicationContext(context);

		// 봇 구동
		ManalithBot bot = context.getBean(ManalithBot.class);
		bot.startBot();
	}
}
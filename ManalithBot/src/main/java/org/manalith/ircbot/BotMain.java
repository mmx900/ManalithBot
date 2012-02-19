/*
 	org.manalith.ircbot/BotMain.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2005, 2011, 2012  Ki-Beom, Kim

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
import java.util.StringTokenizer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class BotMain {
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

		String configFile = "config.xml";
		String configFileOptionArg = cmd.getOptionValue("c");
		if (StringUtils.isNotBlank(configFileOptionArg)) {
			configFile = configFileOptionArg;
		}

		// 설정 초기화
		ApplicationContext context = new FileSystemXmlApplicationContext(
				configFile);

		ConfigurationManager config = context
				.getBean(ConfigurationManager.class);

		// 봇 구동
		final ManalithBot bot = context.getBean(ManalithBot.class);
		bot.setNickname(config.getBotName());
		bot.setVerbose(config.getVerbose());
		bot.setEncoding(config.getServerEncoding());
		bot.connect(config.getServer(), config.getServerPort());

		final StringTokenizer st = new StringTokenizer(
				config.getDefaultChannels(), ",");
		while (st.hasMoreTokens())
			bot.joinChannel(st.nextToken());
	}
}

/*
 	org.manalith.ircbot/BotMain.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2005, 2011, 2012  Ki-Beom, Kim
 	Copyright (C) 2012 Seong-ho, Cho <darkcircle.0426@gmail.com>

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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class BotMain {
	public static void main(String[] args) throws Exception {
		// 인코딩을 검사한다.
		if (!Charset.defaultCharset().toString().equals("UTF-8")) {
			System.out.println("-Dfile.encoding=UTF-8 옵션으로 실행시켜 주세요.");
			return;
		}

		// 옵션을 가져온다.
		Options options = new Options();
		options.addOption("c", true, "config file");

		CommandLineParser parser = new PosixParser();
		CommandLine cmd = parser.parse(options, args);

		String configFile = cmd.getOptionValue("c");
		if (configFile != null) {
			ConfigurationManager.setConfigFile(configFile);
		}

		// 설정 초기화
		ConfigurationManager config = ConfigurationManager.getInstance();

		ApplicationContext context = new FileSystemXmlApplicationContext(
				"springcontext.xml");

		
		// 이벤트 리스너 준비

		
		
		// 봇 구동
		final ManalithBot bot = context.getBean(ManalithBot.class);
		
		// 이벤트 리스너 등록
		ManalithBotEvent botEvent = new ManalithBotEvent( bot.getOwners(), bot.getLogger(), bot.getPluginManager() );
		bot.getListenerManager().addListener(botEvent);
		
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

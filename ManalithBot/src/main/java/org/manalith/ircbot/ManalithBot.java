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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.manalith.ircbot.plugin.EventDispatcher;
import org.manalith.ircbot.plugin.relay.RelayPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.manalith.ircbot.util.AppContextUtil;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.exception.NickAlreadyInUseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ManalithBot extends PircBotX {
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private Configuration configuration;

	@Autowired
	private EventDispatcher eventDispatcher;

	@PostConstruct
	public void onPostConstruct() {
		getListenerManager().addListener(eventDispatcher);
	}

	public static ManalithBot getInstance() {
		return AppContextUtil.getApplicationContext()
				.getBean(ManalithBot.class);
	}

	/**
	 * Configuration으로부터 설정을 읽어들여 봇을 시작한다.
	 * 
	 * @throws Exception
	 *             봇을 설정하고 구동할 때 발생하는 예외
	 */
	public void start() throws IOException, IrcException {
		setLogin(configuration.getBotLogin());
		setName(configuration.getBotName());
		setVerbose(configuration.isVerbose());

		try {
			setEncoding(configuration.getServerEncoding());
		} catch (UnsupportedEncodingException e) {
			logger.error("지원되지 않는 인코딩입니다.");
			return;
		}

		try {
			connect(configuration.getServer(), configuration.getServerPort());
		} catch (NickAlreadyInUseException e) {
			logger.error("닉네임이 이미 사용중입니다.");
			return;
		} catch (IOException | IrcException e) {
			throw e;
		}

		StringTokenizer st = new StringTokenizer(
				configuration.getDefaultChannels(), ",");
		while (st.hasMoreTokens())
			joinChannel(st.nextToken());
	}

	public void invokeMessageEvent(MessageEvent event) {
		eventDispatcher.dispatchMessageEvent(event);
	}

	/**
	 * sendMessage(target, message)가 final 메서드이므로 로깅을 위해 이 메시지를 사용한다.
	 * 
	 * @param target
	 * @param message
	 */
	public void sendLoggedMessage(String target, String message,
			boolean redirectToRelayBot) {
		// TODO sendMessage 오버라이드
		logger.trace(String.format("MESSAGE(LOCAL) : %s / %s", target, message));

		sendMessage(target, message);

		if (redirectToRelayBot && RelayPlugin.RELAY_BOT != null)
			RelayPlugin.RELAY_BOT.sendMessage(target, message);
	}

	public void sendLoggedMessage(String target, String message) {
		sendLoggedMessage(target, message, true);
	}

	// private void sendMessage(Message m){
	// //너무 긴 문자는 자른다.
	// if(m.getMessage().length() > 180)
	// m.setMessage(m.getMessage().substring(0, 179));
	// sendMessage(m.getChannel(), m.getSender() + ", " + m.getMessage());
	// }
}
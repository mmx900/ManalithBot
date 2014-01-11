/*
 org.manalith.ircbot.plugin.keyseqconv/KeySeqConvPlugin.java
 ManalithBot - An open source IRC bot based on the PircBot Framework.
 Copyright (C) 2012 Seong-ho, Cho <darkcircle.0426@gmail.com>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.manalith.ircbot.plugin.keyseqconv;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

@Component("keySeqConvPlugin")
public class KeySeqConvPlugin extends SimplePlugin {

	private Logger logger = Logger.getLogger(getClass());
	private DubeolAutomataEngine dengine;
	private SebeolFinalAutomataEngine sfengine;
	private Sebeol390AutomataEngine stengine;
	private SebeolNoSftAutomataEngine snengine;
	private boolean enableParsingExceptionSyntax;

	public KeySeqConvPlugin() {
		dengine = new DubeolAutomataEngine();
		sfengine = new SebeolFinalAutomataEngine();
		stengine = new Sebeol390AutomataEngine();
		snengine = new SebeolNoSftAutomataEngine();
	}

	public void setEnableParsingExceptionSyntax(boolean enable) {
		enableParsingExceptionSyntax = enable;
	}

	@Override
	public String getName() {
		return "입력 변환기(2벌,3벌[최종,390,순아래])";
	}

	@Override
	public String getCommands() {
		return "!c2|!c3|!c33|!c3n|!c2r";
	}

	@Override
	public String getHelp() {
		return "!c2|!c3|!c33|!c3n 영문문자열, \\변환하지 않을 영문문자열\\ (백슬래시 표시: \\\\) | !c2r 한글문자열";
	}

	@Override
	public void onMessage(MessageEvent event) {
		String msg = event.getMessage();
		String sender = event.getUser().getNick();

		dengine.setEnableParsingExceptionSyntax(enableParsingExceptionSyntax);
		sfengine.setEnableParsingExceptionSyntax(enableParsingExceptionSyntax);
		stengine.setEnableParsingExceptionSyntax(enableParsingExceptionSyntax);
		snengine.setEnableParsingExceptionSyntax(enableParsingExceptionSyntax);

		String cmd = msg.split("\\s")[0];

		if (cmd.equals("!c2")) {
			String srcmsg = msg.substring(msg.indexOf(' ') + 1, msg.length());
			try {
				String dstmsg = dengine.parseKeySequenceToKorean(srcmsg);

				event.respond(String.format("<%s> %s", sender, dstmsg));
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			}
		} else if (cmd.equals("!c2r")) {
			String srcmsg = msg.substring(msg.indexOf(' ') + 1, msg.length());
			String dstmsg = dengine.parseKoreanStringToEngSpell(srcmsg);

			event.respond(String.format("<%s> %s", sender, dstmsg));
		} else if (cmd.equals("!c3")) {
			String srcmsg = msg.substring(msg.indexOf(' ') + 1, msg.length());
			try {
				String dstmsg = sfengine.parseKeySequenceToKorean(srcmsg);

				event.respond(String.format("<%s> %s", sender, dstmsg));
			} catch (IllegalArgumentException | ParseException e) {
				logger.warn(e.getMessage(), e);
			}
		} else if (cmd.equals("!c33")) {
			String srcmsg = msg.substring(msg.indexOf(' ') + 1, msg.length());

			try {
				String dstmsg = stengine.parseKeySequenceToKorean(srcmsg);

				event.respond(String.format("<%s> %s", sender, dstmsg));
			} catch (IllegalArgumentException | ParseException e) {
				logger.warn(e.getMessage(), e);
			}
		} else if (cmd.equals("!c3n")) {
			String srcmsg = msg.substring(msg.indexOf(' ') + 1, msg.length());

			try {
				String dstmsg = snengine.parseKeySequenceToKorean(srcmsg);

				event.respond(String.format("<%s> %s", sender, dstmsg));
			} catch (IllegalArgumentException | ParseException e) {
				logger.warn(e.getMessage(), e);
			}
		}
	}
}

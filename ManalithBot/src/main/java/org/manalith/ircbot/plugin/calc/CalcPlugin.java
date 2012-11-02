/*
 	org.manalith.ircbot.plugin.calc/CalcPlugin.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2011  Seong-ho, Cho <darkcircle.0426@gmail.com>

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
package org.manalith.ircbot.plugin.calc;

import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

@Component
public class CalcPlugin extends AbstractBotPlugin {

	public String getName() {
		return "뒷북계산기";
	}

	public String getCommands() {
		return "!계산";
	}

	public String getHelp() {
		return "설  명: 계산식을 입력하면 답을 구해줍니다, 사용법: !계산 (계산식), sin(), cos(), tan(), arcsin(), arccos(), arctan(), tobin(정수계산식), tooct(정수계산식), todec(정수계산식), tohex(정수계산식)";
	}

	public void onMessage(MessageEvent event) {
		onMessage(event, event.getChannel().getName());
	}

	public void onPrivateMessage(MessageEvent event) {
		onMessage(event, event.getUser().getNick());
	}

	protected void onMessage(MessageEvent event, String target) {
		ManalithBot bot = event.getBot();
		String message = event.getMessage();
		String[] command = message.split("\\s");

		if (command[0].equals("!계산")) {
			if (command.length == 1) {
				bot.sendMessage(target, "입력한 식이 없습니다.");
				bot.sendMessage(target, this.getHelp());
			} else {
				String expr = "";
				for (int i = 1; i < command.length; i++) {
					expr += command[i];
				}
				bot.sendMessage(target, CalcRunner.run(expr));
			}
			event.setExecuted(true);
		}
	}
}

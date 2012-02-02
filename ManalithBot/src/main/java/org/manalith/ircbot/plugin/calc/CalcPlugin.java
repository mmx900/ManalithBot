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

import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEventData;

public class CalcPlugin extends AbstractBotPlugin {

	public String getName() {
		return "뒷북 계산기";
	}

	public String getCommands() {
		return "계산|eval";
	}

	public String getHelp() {
		return "!(계산|eval) (계산식), sin(), cos(), tan(), arcsin(), arccos(), arctan(), tobin(정수계산식), tooct(정수계산식), todec(정수계산식), tohex(정수계산식)";
	}
	//*
	public void onMessage(MessageEventData event) {
		String message = event.getMessage();
		String channel = event.getChannel();
		String[] command = message.split("\\s");

		if (command[0].equals("!계산") || command[0].equals("!eval")) {
			if (command.length == 1) {
				bot.sendLoggedMessage(channel, "입력한 식이 없습니다.");
				bot.sendLoggedMessage(channel, this.getHelp());
			} else {
				String expr = "";
				for (int i = 1; i < command.length; i++) {
					expr += command[i];
				}
				bot.sendLoggedMessage(channel, CalcRunner.run(expr));
			}
			event.setExecuted(true);
		}
	}
	//*/
	public void onPrivateMessage(MessageEventData event)
	{
		String message = event.getMessage();
		String sender = event.getSender();
		
		String[] command = message.split("\\s");
		
		if (command[0].equals("!계산") || command[0].equals("!eval")) {
			if (command.length == 1) {
				bot.sendLoggedMessage(sender, "입력한 식이 없습니다.");
				bot.sendLoggedMessage(sender, this.getHelp());
			} else {
				String expr = "";
				for (int i = 1; i < command.length; i++) {
					expr += command[i];
				}
				bot.sendLoggedMessage(sender, CalcRunner.run(expr));
			}
			event.setExecuted(true);
		}
	}
}

/*
	org.manalith.ircbot.plugin.curex/CurexPlugin.java
	ManalithBot - An open source IRC bot based on the PircBot Framework.
	Copyright (C) 2011, 2012 Seong-ho, Cho <darkcircle.0426@gmail.com>
	Copyright (C) 2012  Changwoo Ryu <cwryu@debian.org>

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
package org.manalith.ircbot.plugin.curex;

import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

@Component("cerPlugin")
public class CurexPlugin extends AbstractBotPlugin {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.manalith.ircbot.plugin.IBotPlugin#getName()
	 */
	public String getName() {
		return "환율계산";
		// return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.manalith.ircbot.plugin.IBotPlugin#getNamespace()
	 */

	public String getCommands() {
		return "!환율";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.manalith.ircbot.plugin.IBotPlugin#getHelp()
	 */
	public String getHelp() {
		return "설  명: 환율 정보를 보여주고 환율 계산을 도와줍니다, 사용법: !환율 (자세한 사용법은 !curex help를 실행하세요)";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.manalith.ircbot.plugin.IBotPlugin#onMessage(org.manalith.ircbot.resources
	 * .MessageEvent)
	 */
	public void onMessage(MessageEvent event) {
		parseMessage(event, event.getChannel().getName());
	}

	public void onPrivateMessage(MessageEvent event) {
		parseMessage(event, event.getUser().getNick());
	}

	protected void parseMessage(MessageEvent event, String target) {
		String[] command = event.getMessageSegments();
		if (!command[0].equals("!환율") && !command[0].startsWith("!환율:"))
			return;

		String[] subcmd = command[0].split("\\:");
		if (subcmd.length == 1) {

			String[] mergedcmd = new String[command.length - 1];
			System.arraycopy(command, 1, mergedcmd, 0, command.length - 1);

			try {
				CurexRunner runner = new CurexRunner(event.getUser().getNick(),
						this.getResourcePath(), mergedcmd);

				String result = runner.run();
				if (result.equals("Help!")) {
					event.respond(CurexInfoProvider.getIRCHelpMessagePart1());
					event.respond(CurexInfoProvider.getIRCHelpMessagePart2());
				} else if (result.equals("unitlist")) {
					event.respond(CurexInfoProvider.getUnitListPart1());
					event.respond(CurexInfoProvider.getUnitListPart2());
				} else {
					event.respond(result);
				}
			} catch (Exception e) {
				event.respond(e.getMessage());
			}
		} else if (subcmd.length > 2) {
			event.respond("옵션이 너무 많습니다");
		} else {
			// remerge strings separated by space.
			String userNick = event.getUser().getNick();

			String arg = "";
			for (int i = 1; i < command.length; i++) {
				if (command[i].equals(" "))
					continue;
				arg += command[i];
			}

			CurexCustomSettingManager csMan = new CurexCustomSettingManager(
					this.getResourcePath(), target, userNick, arg);

			if (subcmd[1].equals("sub"))
				event.respond(csMan.addUserSetting());
			else if (subcmd[1].equals("unsub")) {
				event.respond(csMan.removeUserSetting());
			} else
				event.respond("그런 옵션은 없습니다.");

		}
	}
}

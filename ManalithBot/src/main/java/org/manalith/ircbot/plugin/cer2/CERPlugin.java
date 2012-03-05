/*
	org.manalith.ircbot.plugin.cer2/CERPlugin.java
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
package org.manalith.ircbot.plugin.cer2;

import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

@Component("cerPlugin")
public class CERPlugin extends AbstractBotPlugin {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.manalith.ircbot.plugin.IBotPlugin#getName()
	 */
	public String getName() {
		return "환율 계산기";
		// return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.manalith.ircbot.plugin.IBotPlugin#getNamespace()
	 */

	public String getCommands() {
		return "환율|curex";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.manalith.ircbot.plugin.IBotPlugin#getHelp()
	 */
	public String getHelp() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.manalith.ircbot.plugin.IBotPlugin#onMessage(org.manalith.ircbot.resources
	 * .MessageEvent)
	 */
	public void onMessage(MessageEvent event) {
		onMessage(event, event.getChannel());
	}

	public void onPrivateMessage(MessageEvent event) {
		onMessage(event, event.getSender());
	}

	protected void onMessage(MessageEvent event, String target) {
		String msg = event.getMessage();
		String[] command = msg.split("\\s");
		if (!command[0].equals("!curex") && !command[0].equals("!환율")
				&& !command[0].startsWith("!curex:")
				&& !command[0].startsWith("!환율:"))
			return;

		String[] subcmd = command[0].split("\\:");
		if (subcmd.length == 1) {
			
			String [] mergedcmd = new String[command.length - 1];
			System.arraycopy(command, 1, mergedcmd, 0, command.length - 1);

			try {
				CERRunner runner = new CERRunner(event.getSender(),
						this.getResourcePath(), mergedcmd);

				String result = runner.run();
				if (result.equals("Help!")) {
					bot.sendLoggedMessage(target, CERInfoProvider.getIRCHelpMessagePart1());
					bot.sendLoggedMessage(target, CERInfoProvider.getIRCHelpMessagePart2());
				} else if ( result.equals("unitlist")) {
					bot.sendLoggedMessage(target, CERInfoProvider.getUnitListPart1());
					bot.sendLoggedMessage(target,
							CERInfoProvider.getUnitListPart2());
				} else {
					bot.sendLoggedMessage(target, result);
				}
			} catch (Exception e) {
				bot.sendLoggedMessage(target, e.getMessage());
			}
		} else if (subcmd.length > 2) {
			bot.sendLoggedMessage(target, "옵션이 너무 많습니다");
		} else {
			// remerge strings separated by space.
			String userNick = event.getSender();

			String arg = "";
			for (int i = 1; i < command.length; i++) {
				if (command[i].equals(" "))
					continue;
				arg += command[i];
			}

			CERCustomSettingManager csMan = new CERCustomSettingManager(
					this.getResourcePath(), target, userNick, arg);

			if (subcmd[1].equals("sub"))
				bot.sendLoggedMessage(target, csMan.addUserSetting());
			else if (subcmd[1].equals("unsub")) {
				bot.sendLoggedMessage(target, csMan.removeUserSetting());
			} else
				bot.sendLoggedMessage(target, "그런 옵션은 없습니다.");

		}
		event.setExecuted(true);
	}
}

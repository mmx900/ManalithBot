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

import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;

public class CERPlugin extends AbstractBotPlugin {

	public CERPlugin(ManalithBot bot) {
		super(bot);
	}

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
		return "curex|환율";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.manalith.ircbot.plugin.IBotPlugin#getHelp()
	 */
	public String getHelp() {
		/*
		 * String result =
		 * "!curex ( [Option] [Currency_Unit] [FieldAbbr/Amount] ) [Option] : show, lastround, convfrom, convto, buycash, cellcash, sendremit, recvremit "
		 * ; result +=
		 * "[Currency_Unit] : USD:U.S, EUR:Europe, JPY:Japan, CNY:China, HKD:HongKong, TWD:Taiwan, GBP:Great Britain, CAD:Canada, CHF:Switzerland, SEK:Sweden, "
		 * ; result +=
		 * "AUD:Australia, NZD:NewZealand, ISL:Israel, DKK:Denmark, NOK:Norway, SAR:Saudi Arabia, KWD:Kuweit, BHD:Bahrain, AED:United of Arab Emirates, "
		 * ; result +=
		 * "JOD:Jordan, EGP:Egypt, THB:Thailand, SGD:Singapore, MYR:Malaysia, IDR:Indonesia, BND:Brunei, INR:India, PKR:Pakistan, BDT:Bangladesh, PHP:Philippine, "
		 * ; result +=
		 * "MXN:Mexico, BRL:Brazil, VND:Vietnam, ZAR:Republic of South Africa, RUB:Russia, HUF:Hungary, PLN:Poland "
		 * ; result +=
		 * "[FieldAbbr] (show 명령에만 해당) 모두보기, 매매기준, 현찰매수, 현찰매도, 송금보냄, 송금받음, 환수수료, 대미환산 "
		 * ; result += "[Amount] 금액";
		 */
		return "(null)";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.manalith.ircbot.plugin.IBotPlugin#onMessage(org.manalith.ircbot.resources
	 * .MessageEvent)
	 */
	public void onMessage(MessageEvent event) {
		String msg = event.getMessage();
		String channel = event.getChannel();

		String[] command = msg.split("\\s");
		if (!command[0].equals("!curex") && !command[0].equals("!환율")
				&& !command[0].startsWith("!curex:")
				&& !command[0].startsWith("!환율:"))
			return;

		String[] subcmd = command[0].split("\\:");
		if (subcmd.length == 1) {
			String mergedcmd = "";
			for (int i = 1; i < command.length; i++) {
				mergedcmd += command[i];
				if (i != command.length - 1)
					mergedcmd += " ";
			}

			try {
				CERRunner runner = new CERRunner(event.getSender(),
						this.getResourcePath(), mergedcmd);

				String result = runner.run();
				if (result.equals("Help!")) {
					/*
					 * bot.sendLoggedMessage(channel,
					 * CERInfoProvider.getIRCHelpMessagePart1());
					 * bot.sendLoggedMessage(channel,
					 * CERInfoProvider.getIRCHelpMessagePart2());
					 * bot.sendLoggedMessage(channel,
					 * CERInfoProvider.getIRCHelpMessagePart3());
					 * bot.sendLoggedMessage(channel,
					 * CERInfoProvider.getIRCHelpMessagePart4());
					 */
					bot.sendLoggedMessage(channel, "도움말 그런거 없음!");

				} else {
					bot.sendLoggedMessage(channel, result);
				}
			} catch (Exception e) {
				bot.sendLoggedMessage(channel, e.getMessage());
			}
		} else if (subcmd.length > 2) {
			bot.sendLoggedMessage(channel, "옵션이 너무 많습니다");
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
					this.getResourcePath(), channel, userNick, arg);

			if (subcmd[1].equals("sub"))
				bot.sendLoggedMessage(channel, csMan.addUserSetting());
			else if (subcmd[1].equals("unsub")) {

				bot.sendLoggedMessage(channel, csMan.removeUserSetting());

				bot.sendLoggedMessage(channel, csMan.removeUserSetting());
			} else
				bot.sendLoggedMessage(channel, "그런 옵션은 없습니다.");

		}
		event.setExecuted(true);
	}
}

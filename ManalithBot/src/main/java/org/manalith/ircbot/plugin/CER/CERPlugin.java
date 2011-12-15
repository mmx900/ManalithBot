//
// CERPlugin.java
// darkcircle dot 0426 at gmail dot com
//
// This source can be distributed under the terms of GNU General Public License version 3
// which is derived from the license of Manalith bot.
package org.manalith.ircbot.plugin.CER;

import org.manalith.ircbot.BotMain;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.manalith.ircbot.plugin.CER.Exceptions.InvalidArgumentException;

public class CERPlugin extends AbstractBotPlugin {


	public CERPlugin(BotMain bot) {
		super(bot);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.manalith.ircbot.plugin.IBotPlugin#getName()
	 */
	public String getName() {
		// TODO Auto-generated method stub
		return "환율 계산기";
		//return null;
	}

	/* (non-Javadoc)
	 * @see org.manalith.ircbot.plugin.IBotPlugin#getNamespace()
	 */
	public String getNamespace() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.manalith.ircbot.plugin.IBotPlugin#getHelp()
	 */
	public String getHelp() {
		String result = "!cer ( [Option] [Currency_Unit] [FieldAbbr/Amount] ) [Option] : show, lastround, convfrom, convto, buycash, cellcash, sendremit, recvremit ";
		result += "[Currency_Unit] : USD:U.S, EUR:Europe, JPY:Japan, CNY:China, HKD:HongKong, TWD:Taiwan, GBP:Great Britain, CAD:Canada, CHF:Switzerland, SEK:Sweden, ";
		result += "AUD:Australia, NZD:NewZealand, ISL:Israel, DKK:Denmark, NOK:Norway, SAR:Saudi Arabia, KWD:Kuweit, BHD:Bahrain, AED:United of Arab Emirates, ";
		result += "JOD:Jordan, EGP:Egypt, THB:Thailand, SGD:Singapore, MYR:Malaysia, IDR:Indonesia, BND:Brunei, INR:India, PKR:Pakistan, BDT:Bangladesh, PHP:Philippine, ";
		result += "MXN:Mexico, BRL:Brazil, VND:Vietnam, ZAR:Republic of South Africa, RUB:Russia, HUF:Hungary, PLN:Poland ";
		result += "[FieldAbbr] (show 명령에만 해당) 모두보기, 매매기준, 현찰매수, 현찰매도, 송금보냄, 송금받음, 환수수료, 대미환산 ";
		result += "[Amount] 금액";
		return result;
	}

	/* (non-Javadoc)
	 * @see org.manalith.ircbot.plugin.IBotPlugin#onJoin(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void onJoin(String channel, String sender, String login,
			String hostname) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.manalith.ircbot.plugin.IBotPlugin#onMessage(org.manalith.ircbot.resources.MessageEvent)
	 */
	public void onMessage(MessageEvent event) {
		// TODO Auto-generated method stub
		String msg = event.getMessage();
		String channel = event.getChannel();
		
		String [] command = msg.split("\\s");
		if ( command[0].equals("!cer") )
		{
			String mergedcmd = "";
			for ( int i = 1; i < command.length ; i++ )
			{
				mergedcmd += command[i];
				if ( i != command.length - 1 ) mergedcmd += " ";
			}
			
			CERMessageTokenAnalyzer ta = new CERMessageTokenAnalyzer ( mergedcmd );
			String ConvertedCommand = "";
		
			try 
			{
				ConvertedCommand = ta.convertToCLICommandString();
				CERRunner runner = new CERRunner ( this.getResourcePath(), ConvertedCommand );
				
				String result = runner.run();
				if ( result.equals("Help!") )
				{
					BotMain.BOT.sendLoggedMessage(channel, CERInfoProvider.getIRCHelpMessagePart1());
					BotMain.BOT.sendLoggedMessage(channel, CERInfoProvider.getIRCHelpMessagePart2());
				}
				else
				{
					BotMain.BOT.sendLoggedMessage(channel, result);
				}
			}
			catch( InvalidArgumentException ae )
			{
				BotMain.BOT.sendLoggedMessage(channel, ae.getMessage());
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.manalith.ircbot.plugin.IBotPlugin#onPart()
	 */
	public void onPart() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.manalith.ircbot.plugin.IBotPlugin#onQuit()
	 */
	public void onQuit() {
		// TODO Auto-generated method stub

	}

}

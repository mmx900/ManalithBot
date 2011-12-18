package org.manalith.ircbot.plugin.Calc;

// import org.jibble.pircbot.User;
import org.manalith.ircbot.BotMain;
import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;

public class CalcPlugin extends AbstractBotPlugin {

	
	public CalcPlugin(ManalithBot bot) {
		super(bot);
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return "뒷북 계산기";
	}

	public String getNamespace() {
		// TODO Auto-generated method stub
		return "eval";
	}

	public String getHelp() {
		return "!eval (계산식), sin(), cos(), tan(), arcsin(), arccos(), arctan(), tobin(정수계산식), tooct(정수계산식), todec(정수계산식), tohex(정수계산식)";
	}

	public void onJoin(String channel, String sender, String login,
			String hostname) {
		// TODO Auto-generated method stub
	}

	public void onMessage(MessageEvent event) {
		// TODO Auto-generated method stub
		String message = event.getMessage();
		String channel = event.getChannel();
		String [] command = message.split("\\s");
		
		if ( command[0].equals("!eval") )
		{
			if ( command.length == 1 )
			{
				BotMain.BOT.sendLoggedMessage(channel, "입력한 식이 없습니다.");
			}
			else 
			{
				String expr = "";
				for ( int i = 1 ; i < command.length ; i++ )
				{
					expr += command[i];
				}
				BotMain.BOT.sendLoggedMessage(channel, CalcRunner.run(expr));
			}
		}

	}

	public void onPart() {
		// TODO Auto-generated method stub

	}

	public void onQuit() {
		// TODO Auto-generated method stub

	}

}

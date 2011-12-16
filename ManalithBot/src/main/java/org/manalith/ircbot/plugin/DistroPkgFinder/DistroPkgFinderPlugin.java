package org.manalith.ircbot.plugin.DistroPkgFinder;

import org.manalith.ircbot.BotMain;
import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;

public class DistroPkgFinderPlugin extends AbstractBotPlugin {

	public DistroPkgFinderPlugin(ManalithBot bot) {
		super(bot);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "뒷북 디스트로 패키지 검색";
	}

	@Override
	public String getNamespace() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getHelp () 
	{
		return "!deb (pkg_name) | !ubu (pkg_name) | !fed (pkg_name)";
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
		
		if ( command.length > 2 )
		{
			BotMain.BOT.sendLoggedMessage(channel, "검색 단어는 하나면 충분합니다.");
		}
		else if ( command[0].equals("!deb") )
		{
			DebianPkgFinderRunner runner = new DebianPkgFinderRunner ( command[1] );
			BotMain.BOT.sendLoggedMessage(channel, runner.run());
		}
		else if ( command[0].equals("!ubu") )
		{
			UbuntuPkgFinderRunner runner = new UbuntuPkgFinderRunner ( command[1] );
			BotMain.BOT.sendLoggedMessage(channel, runner.run());
		}
		else if ( command[0].equals("!fed") )
		{
			FedoraPkgFinderRunner runner = new FedoraPkgFinderRunner ( command[1] );
			BotMain.BOT.sendLoggedMessage(channel, runner.run());
		}

	}
	
	public void onPart() {
		// TODO Auto-generated method stub

	}

	public void onQuit() {
		// TODO Auto-generated method stub

	}
}

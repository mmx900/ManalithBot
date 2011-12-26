package org.manalith.ircbot.plugin.TwitReader;

import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;

public class TwitReaderPlugin extends AbstractBotPlugin {

	public TwitReaderPlugin(ManalithBot bot) {
		super(bot);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "뒷북 트윗리더";
	}

	@Override
	public String getNamespace() {
		// TODO Auto-generated method stub
		return "twit";
	}
	
	public String getHelp ()
	{
		return "!twit (URL)";
	}
	
	@Override
	public void onMessage(MessageEvent event) {
		// TODO Auto-generated method stub
		String msg = event.getMessage();
		String channel = event.getChannel();
		
		String [] command = msg.split("\\s");
		
		if ( command[0].equals("!twit") )
		{
			if ( command.length == 1 )
			{
				bot.sendLoggedMessage(channel, this.getHelp());
			}
			else if ( command.length > 2 )
			{
				bot.sendLoggedMessage(channel, "불필요한 옵션이 있습니다.");
			}
			else
			{
				TwitReaderRunner runner = new TwitReaderRunner ( command[1] );
				String [] result = runner.run();
				
				int length = result.length;
				
				for ( int i = 0 ; i < length ; i++ )
				{
					bot.sendLoggedMessage(channel, result[i] );
				}
			}
		}
	}
	
}

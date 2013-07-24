import org.manalith.ircbot.plugin.SimplePlugin
import org.manalith.ircbot.resources.MessageEvent


class HelloGroovyPlugin extends SimplePlugin {
	private final String COMMAND = "!helloGroovy";

	@Override
	public String getName() {
		return "Sample groovy plugin";
	}

	@Override
	public String getCommands() {
		return COMMAND;
	}

	@Override
	public void onMessage(MessageEvent event) {
		if(COMMAND.equals(event.getMessage()))
			event.respond("Hello Groovy!");
	}
}

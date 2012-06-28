package org.manalith.ircbot.plugin.linuxpkgfinder;

import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;

public abstract class PackageFinder extends AbstractBotPlugin {
	public abstract String find(MessageEvent event, String... args);

	public abstract String find(String arg);
}

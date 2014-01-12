package org.manalith.ircbot.plugin.linuxpkgfinder;

import org.manalith.ircbot.plugin.SimplePlugin;

public abstract class PackageFinder extends SimplePlugin {

	public abstract String find(String arg);
}

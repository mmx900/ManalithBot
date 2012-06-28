package org.manalith.ircbot.plugin.linuxpkgfinder;

import org.manalith.ircbot.resources.MessageEvent;

public interface GentooSearchEngineProvider {
    public String find(MessageEvent event, String... args);

    public String find(String arg);
}

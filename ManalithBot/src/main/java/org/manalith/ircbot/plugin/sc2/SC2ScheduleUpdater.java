package org.manalith.ircbot.plugin.sc2;

import java.io.IOException;

public interface SC2ScheduleUpdater {

	String getUrl();

	void update() throws IOException;
}

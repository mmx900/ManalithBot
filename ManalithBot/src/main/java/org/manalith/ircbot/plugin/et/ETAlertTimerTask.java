package org.manalith.ircbot.plugin.et;

import java.util.Calendar;
import java.util.TimerTask;

import org.manalith.ircbot.ManalithBot;

public class ETAlertTimerTask extends TimerTask {

	public final ETPlugin plugin;

	public ETAlertTimerTask(ETPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * 한 시간에 한 번씩 실행되어야 한다.
	 */
	@Override
	public void run() {
		Calendar now = Calendar.getInstance();

		if (now.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
				|| now.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			if (now.get(Calendar.HOUR_OF_DAY) == 22)
				ManalithBot
						.getInstance()
						.sendIRC()
						.action("#gnome",
								"10시! 주말버전 ET 하자옹! "
										+ plugin.getPlayerManager()
												.getPlayerNicks());
		} else {
			if (now.get(Calendar.HOUR_OF_DAY) == 23)
				ManalithBot
						.getInstance()
						.sendIRC()
						.action("#gnome",
								"11시! ET 하자옹! "
										+ plugin.getPlayerManager()
												.getPlayerNicks());
		}
	}
}

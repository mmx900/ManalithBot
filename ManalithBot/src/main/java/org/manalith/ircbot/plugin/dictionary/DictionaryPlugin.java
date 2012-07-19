package org.manalith.ircbot.plugin.dictionary;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.command.CommandParser;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DictionaryPlugin extends AbstractBotPlugin {
	@Autowired
	private DictionaryManager dictionaryManager;

	public DictionaryManager getDictionaryManager() {
		return dictionaryManager;
	}

	public void setDictionaryManager(DictionaryManager dictionaryManager) {
		this.dictionaryManager = dictionaryManager;
	}

	public String getName() {
		return "FAQ 플러그인";
	}

	public String getCommands() {
		return "배워";
	}

	public String getHelp() {
		return "사용법 : 배워 [단어] [해석]";
	}

	public void onMessage(MessageEvent event) {
		ManalithBot bot = event.getBot();
		String message = event.getMessage();
		String sender = event.getUser().getNick();

		String cmd = CommandParser.checkMessageAndRemoveNick(bot.getName(),
				message);

		if (cmd != null) {

			if (cmd.equals("그치?")) {
				if (((int) (Math.random() * 10)) % 2 == 0)
					event.respond("응응!");
				else
					event.respond("(먼산)");
			} else {
				Word w = null;
				if (cmd.startsWith("배워 ")) {
					String[] arr = cmd.split(" ");

					if (arr.length >= 3) {
						w = new Word();
						w.word = arr[1];
						if (arr.length > 3)
							w.description = StringUtils.join(arr, ' ', 2,
									arr.length);
						else
							w.description = arr[2];
						w.author = sender;
						w.date = new Date();
						dictionaryManager.add(w);

						event.respond("단어를 배웠습니다.");
					} else {
						event.respond(getHelp());
					}

				} else if ((w = dictionaryManager.getWord(cmd)) != null) {
					event.respond("[" + w.word + "] " + w.description + " -"
							+ w.author + "("
							+ DateFormatUtils.format(w.date, "yyyy-MM-dd")
							+ ")");
				} else {
					event.respond("(먼산)");
				}
			}
		}
	}

}

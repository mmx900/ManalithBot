package org.manalith.ircbot.plugin.dictionary;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.manalith.ircbot.command.CommandParser;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.beans.factory.annotation.Autowired;

public class DictionaryPlugin extends AbstractBotPlugin {
	private DictionaryManager dict;
	
	@Autowired
	private WordDao wordDao;
	
	public WordDao getWordDao(){
		return wordDao;
	}
	
	public void setWordDao(WordDao wordDao){
		this.wordDao = wordDao;
	}

	public DictionaryPlugin() {
		dict = DictionaryManager.instance();
	}

	public String getName() {
		return "FAQ 플러그인";
	}

	public String getCommands() {
		return "배워";
	}

	public String getHelp() {
		return null;
	}

	public void onMessage(MessageEvent event) {
		String message = event.getMessage();
		String channel = event.getChannel();
		String sender = event.getSender();

		String cmd = CommandParser.checkMessageAndRemoveNick(
				bot.getName(), message);

		if (cmd != null) {

			if (cmd.equals("그치?")) {
				if (((int) (Math.random() * 10)) % 2 == 0)
					bot.sendLoggedMessage(channel, "응응!");
				else
					bot.sendLoggedMessage(channel, "(먼산)");
			} else {
				//XXX
				/*
				if (cmd.startsWith("DAO ")){
					Word word = new Word();
					word.setWord("test");
					word.setAuthor("test");
					word.setDescription("test");
					word.setDate(new Date());
					wordDao.save(word);
				}else 
				*/
				if (cmd.startsWith("배워 ")) {
					String[] arr = cmd.split(" ");

					if (arr.length >= 3) {
						Word w = new Word();
						w.word = arr[1];
						if (arr.length > 3)
							w.description = StringUtils.join(arr, ' ', 2,
									arr.length);
						else
							w.description = arr[2];
						w.author = sender;
						w.date = new Date();
						dict.add(w);
						dict.save();

						bot.sendLoggedMessage(channel, "단어를 배웠습니다.");
					} else {
						bot.sendLoggedMessage(channel,
								"사용법 : 배워 [단어] [해석]");
					}

				} else if (dict.hasWord(cmd)) {
					try {
						Word w = dict.getWord(cmd);
						bot.sendLoggedMessage(channel, "[" + w.word
								+ "] " + w.description + " -" + w.author + "("
								+ DateFormatUtils.format(w.date, "yyyy-MM-dd")
								+ ")");
					} catch (NotRegisteredException e) {
						// ignore
					}
				} else {
					bot.sendLoggedMessage(channel, "(먼산)");
				}
			}
		}
	}

}

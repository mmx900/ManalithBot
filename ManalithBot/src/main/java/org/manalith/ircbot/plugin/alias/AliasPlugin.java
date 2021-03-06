package org.manalith.ircbot.plugin.alias;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AliasPlugin extends SimplePlugin {

	@Autowired
	private AliasDao aliasDao;

	@Override
	public String getName() {
		return "별칭";
	}

	@Override
	public String getCommands() {
		return "!alias";
	}

	@Override
	public String getHelp() {
		return "가르치기 : !alias [명령] [스크립트], 보기 : !alias [명령], 사용하기 : ![명령]";
	}

	@Override
	public void onMessage(MessageEvent event) {
		// 악의 혹은 실수에 의한 재귀 호출 alias 명령을 차단한다.
		if (event.isRecursive()) {
			return;
		}

		String message = event.getMessage();
		String sender = event.getUser().getNick();

		Alias a = null;
		if (message.equals("!alias")) {
			event.respond(getHelp());
		} else if (message.startsWith("!alias ")) {
			String[] arr = message.split(" ");

			if (arr.length >= 3) {
				a = new Alias();
				a.alias = arr[1];
				if (arr.length > 3)
					a.script = StringUtils.join(arr, ' ', 2, arr.length);
				else
					a.script = arr[2];
				a.author = sender;
				a.date = new Date();
				aliasDao.save(a);

				event.respond("Alias를 배웠습니다.");
			} else if (arr.length == 2) {
				a = aliasDao.findByWord(arr[1]);
				if (a != null) {
					event.respond(String.format("[%s] %s -%s(%s) ", a.alias,
							a.script, a.author,
							DateFormatUtils.format(a.date, "yyyy-MM-dd")));
				}
			} else {
				event.respond(getHelp());
			}
		} else if (message.startsWith("!")) {
			a = aliasDao.findByWord(StringUtils.substring(message, 1));
			if (a != null) {
				event.setMessage(a.getScript());
				event.restart();
			}
		}
	}
}

package org.manalith.ircbot.plugin.dictionary;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.manalith.ircbot.annotation.Description;
import org.manalith.ircbot.annotation.NotNull;
import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.springframework.stereotype.Component;

import dk.dren.hunspell.Hunspell;

@Component
public class SpellCheckerPlugin extends SimplePlugin {

	private Logger logger = Logger.getLogger(getClass());

	private String englishDictionaryPath;

	private String koreanDictionaryPath;

	public String getEnglishDictionaryPath() {
		return englishDictionaryPath;
	}

	public void setEnglishDictionaryPath(String path) {
		this.englishDictionaryPath = path;
	}

	public String getKoreanDictionaryPath() {
		return koreanDictionaryPath;
	}

	public void setKoreanDictionaryPath(String path) {
		this.koreanDictionaryPath = path;
	}

	@Override
	public String getName() {
		return "맞춤법 검사";
	}

	@BotCommand({ "맞춤법" })
	public String checkKorean(@Description("키워드") @NotNull String keyword) {
		return checkSpell(keyword, koreanDictionaryPath);
	}

	@BotCommand({ "spell" })
	public String checkEnglish(@Description("키워드") @NotNull String keyword) {
		return checkSpell(keyword, englishDictionaryPath);
	}

	private String checkSpell(String keyword, String dictPath) {
		try {
			Hunspell.Dictionary dict = Hunspell.getInstance().getDictionary(
					dictPath);

			if (dict.misspelled(keyword)) {
				List<String> suggestions = dict.suggest(keyword);
				String result = StringUtils.join(suggestions, ' ');
				return StringUtils.isNotBlank(result) ? "추천 단어 : " + result
						: "오류를 찾았으나 추천 단어가 없습니다.";
			}

			return "오류가 없습니다.";
		} catch (FileNotFoundException | UnsupportedEncodingException
				| UnsatisfiedLinkError | UnsupportedOperationException e) {
			logger.error(e.getMessage(), e);

			return "실행중 오류가 발생했습니다. 로그를 참고해주세요.";
		}
	}
}

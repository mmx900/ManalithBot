package org.manalith.ircbot.plugin.dictionary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DictionaryManager {
	@Autowired
	private WordDao wordDao;

	public WordDao getWordDao() {
		return wordDao;
	}

	public void setWordDao(WordDao wordDao) {
		this.wordDao = wordDao;
	}

	public Word getWord(String word) {
		return wordDao.findByWord(word);
	}

	public void add(Word word) {
		Word w = wordDao.findByWord(word.word);

		if (w != null)
			wordDao.delete(w);

		wordDao.save(word);
	}

	public void remove(String word) throws NotRegisteredException {
		Word w = wordDao.findByWord(word);

		if (w == null)
			throw new NotRegisteredException();

		wordDao.delete(w);
	}
}

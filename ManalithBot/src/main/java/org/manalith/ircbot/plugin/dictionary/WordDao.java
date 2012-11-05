package org.manalith.ircbot.plugin.dictionary;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Repository
public class WordDao {

	@Resource
	private SessionFactory sessionFactory;

	@Transactional
	public void save(Word word) {
		sessionFactory.getCurrentSession().save(word);
	}

	@Transactional
	public void delete(Word word) {
		sessionFactory.getCurrentSession().delete(word);
	}

	@Transactional(readOnly = true)
	public Word findByWord(String word) {
		@SuppressWarnings("unchecked")
		List<Word> words = Collections
				.checkedList(
						sessionFactory
								.getCurrentSession()
								.createQuery(
										"from Word where word=:word order by date desc")
								.setParameter("word", word).list(), Word.class);
		if (CollectionUtils.isEmpty(words))
			return null;
		else
			return words.get(0);
	}
}

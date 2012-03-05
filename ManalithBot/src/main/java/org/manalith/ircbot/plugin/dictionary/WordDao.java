package org.manalith.ircbot.plugin.dictionary;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository
public class WordDao extends HibernateDaoSupport {
	@Autowired
	public void anyMethodName(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	public void save(Word word) {
		getHibernateTemplate().save(word);
	}

	public void update(Word word) {
		getHibernateTemplate().update(word);
	}

	public void delete(Word word) {
		getHibernateTemplate().delete(word);
	}

	public Word findByWord(String word) {
		@SuppressWarnings("rawtypes")
		List list = getHibernateTemplate().find("from Word where word=? order by date desc", word);
		return CollectionUtils.isEmpty(list) ? null : (Word) list.get(0);
	}
}

package org.manalith.ircbot.plugin.alias;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository
public class AliasDao extends HibernateDaoSupport {
	@Autowired
	public void anyMethodName(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	public void save(Alias alias) {
		getHibernateTemplate().save(alias);
	}

	public void update(Alias alias) {
		getHibernateTemplate().update(alias);
	}

	public void delete(Alias alias) {
		getHibernateTemplate().delete(alias);
	}

	public Alias findByWord(String alias) {
		@SuppressWarnings("rawtypes")
		List list = getHibernateTemplate().find(
				"from Alias where alias=? order by date desc", alias);
		return CollectionUtils.isEmpty(list) ? null : (Alias) list.get(0);
	}
}

package org.manalith.ircbot.plugin.sc2;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class AbstractHibernateDao<T> {

	@Resource
	private SessionFactory sessionFactory;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	protected Criteria createCriteria() {
		return getSession().createCriteria(getType());
	}

	abstract protected Class<T> getType();
}

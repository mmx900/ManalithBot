package org.manalith.ircbot.plugin.alias;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Repository
public class AliasDao {

	@Resource
	private SessionFactory sessionFactory;

	@Transactional
	public void save(Alias alias) {
		sessionFactory.getCurrentSession().save(alias);
	}

	@Transactional
	public void delete(Alias alias) {
		sessionFactory.getCurrentSession().delete(alias);
	}

	@Transactional(readOnly = true)
	public Alias findByWord(String alias) {
		@SuppressWarnings("unchecked")
		List<Alias> aliases = Collections
				.checkedList(
						sessionFactory
								.getCurrentSession()
								.createQuery(
										"from Alias where alias=:alias order by date desc")
								.setParameter("alias", alias).list(),
						Alias.class);
		if (CollectionUtils.isEmpty(aliases))
			return null;
		else
			return aliases.get(0);
	}
}

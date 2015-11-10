package org.manalith.ircbot.plugin.sc2;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.NullArgumentException;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class SC2EventDao extends AbstractHibernateDao<SC2Event> {

	private Logger log = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<SC2Event> findAll() {
		Criteria criteria = createCriteria();

		criteria.addOrder(Order.desc("date"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<SC2Event> findCurrentEvents() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("KST"));

		calendar.add(Calendar.HOUR_OF_DAY, -4);

		Date from = calendar.getTime();

		calendar.add(Calendar.DAY_OF_MONTH, 1);

		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);

		Date to = calendar.getTime();

		Criteria criteria = createCriteria();

		criteria.add(Restrictions.ge("date", from));
		criteria.add(Restrictions.lt("date", to));

		criteria.addOrder(Order.asc("date"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		log.debug("Retrieving events from {} to {}", from, to);

		return criteria.list();
	}

	@Transactional
	public void add(SC2Event event) {
		if (event == null) {
			throw new NullArgumentException("event");
		}

		Criteria criteria = createCriteria();

		criteria.add(Restrictions.eq("date", event.getDate()));
		criteria.add(Restrictions.eq("title", event.getTitle()));

		for (Object dupe : criteria.list()) {
			log.debug("Deleting a duplicated event: {}", dupe);

			getSession().delete(dupe);
		}

		getSession().persist(event);
	}

	public void deletePastEvents() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("KST"));

		calendar.add(Calendar.DAY_OF_MONTH, -1);

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		Date to = calendar.getTime();

		Criteria criteria = createCriteria();

		criteria.add(Restrictions.lt("date", to));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		for (Object old : criteria.list()) {
			log.debug("Deleting an old event: {}", old);

			getSession().delete(old);
		}
	}

	/**
	 * @see org.manalith.ircbot.plugin.sc2.AbstractHibernateDao#getType()
	 */
	@Override
	protected Class<SC2Event> getType() {
		return SC2Event.class;
	}
}
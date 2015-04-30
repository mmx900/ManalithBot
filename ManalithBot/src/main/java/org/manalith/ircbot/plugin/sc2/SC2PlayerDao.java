package org.manalith.ircbot.plugin.sc2;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.NullArgumentException;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class SC2PlayerDao extends AbstractHibernateDao<SC2Player> {

	@Transactional(readOnly = true)
	public SC2Player findById(String id) {
		if (id == null) {
			throw new NullArgumentException("id");
		}

		return (SC2Player) getSession().get(getType(), id);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<SC2Player> findAll() {
		Criteria criteria = createCriteria();

		criteria.addOrder(Order.asc("name"));

		return criteria.list();
	}

	@Transactional
	public void add(SC2Player player) {
		if (player == null) {
			throw new NullArgumentException("player");
		}

		player.setLastUpdate(new Date());

		getSession().persist(player);
	}

	/**
	 * @see org.manalith.ircbot.plugin.sc2.AbstractHibernateDao#getType()
	 */
	@Override
	protected Class<SC2Player> getType() {
		return SC2Player.class;
	}
}

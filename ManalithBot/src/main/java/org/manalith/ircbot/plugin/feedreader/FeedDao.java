package org.manalith.ircbot.plugin.feedreader;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Repository
public class FeedDao {

	@Resource
	private SessionFactory sessionFactory;

	@Transactional
	public void save(Feed feed) {
		sessionFactory.getCurrentSession().save(feed);
	}

	@Transactional
	public void update(Feed feed) {
		sessionFactory.getCurrentSession().update(feed);
	}

	@Transactional
	public void delete(Feed feed) {
		sessionFactory.getCurrentSession().delete(feed);
	}

	@Transactional(readOnly = true)
	public Feed findByUrl(String url) {
		@SuppressWarnings("unchecked")
		List<Feed> feeds = Collections.checkedList(
				sessionFactory
						.getCurrentSession()
						.createQuery(
								"from Feed where url=:url order by date desc")
						.setParameter("url", url).list(), Feed.class);
		if (CollectionUtils.isEmpty(feeds))
			return null;
		else
			return feeds.get(0);
	}

	@Transactional(readOnly = true)
	public Feed findByUrl(String url, String channel) {
		@SuppressWarnings("unchecked")
		List<Feed> feeds = Collections
				.checkedList(
						sessionFactory
								.getCurrentSession()
								.createQuery(
										"from Feed where url=:url and channel=:channel order by date desc")
								.setParameter("url", url)
								.setParameter("channel", channel).list(),
						Feed.class);
		if (CollectionUtils.isEmpty(feeds))
			return null;
		else
			return feeds.get(0);
	}

	@Transactional(readOnly = true)
	public List<Feed> findAll() {
		@SuppressWarnings("unchecked")
		List<Feed> feeds = Collections
				.checkedList(
						sessionFactory.getCurrentSession()
								.createQuery("from Feed order by date desc")
								.list(), Feed.class);

		return feeds;
	}
}

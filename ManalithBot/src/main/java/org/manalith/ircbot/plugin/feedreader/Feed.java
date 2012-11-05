package org.manalith.ircbot.plugin.feedreader;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Feed {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	public Long id;

	// FIXME url과 channel이 동일할 경우 다시 등록하지 못하도록 제약 필요
	@Column(unique = false, nullable = false)
	public String url;

	@Column(unique = false, nullable = false)
	public String channel;

	@Column(unique = false, nullable = true)
	public String latestContents;

	@Column(unique = false, nullable = false)
	public String user;

	@Column(unique = false, nullable = false)
	public Date date;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the channel
	 */
	public String getChannel() {
		return channel;
	}

	/**
	 * @param channel
	 *            the channel to set
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}

	/**
	 * @return the latestContents
	 */
	public String getLatestContents() {
		return latestContents;
	}

	/**
	 * @param latestContents
	 *            the latestContents to set
	 */
	public void setLatestContents(String latestContents) {
		this.latestContents = latestContents;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
}

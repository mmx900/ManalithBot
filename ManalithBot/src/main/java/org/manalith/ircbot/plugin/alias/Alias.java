package org.manalith.ircbot.plugin.alias;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Alias {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	public Long id;

	@Column(unique = false, nullable = false)
	public String alias;

	@Column(unique = false, nullable = false)
	public String script;

	@Column(unique = false, nullable = false)
	public Date date;

	@Column(unique = false, nullable = false)
	public String author;

	public String getAuthor() {
		return author;
	}

	public Date getDate() {
		return date;
	}

	public String getScript() {
		return script;
	}

	public Long getId() {
		return id;
	}

	public String getAlias() {
		return alias;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return "'" + author + "','" + sdf.format(date.getTime()) + "','"
				+ script + "','" + alias + "'";
	}
}

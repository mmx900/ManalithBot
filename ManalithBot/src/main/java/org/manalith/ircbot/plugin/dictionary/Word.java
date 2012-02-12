package org.manalith.ircbot.plugin.dictionary;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Word {
	public Integer id;
	public String word;
	public String description;
	public Date date;
	public String author;

	public String getAuthor() {
		return author;
	}

	public Date getDate() {
		return date;
	}

	public String getDescription() {
		return description;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	public Integer getId() {
		return id;
	}

	@Column(unique = true, nullable = false)
	public String getWord() {
		return word;
	}

	@Column(unique = false, nullable = false)
	public void setAuthor(String author) {
		this.author = author;
	}

	@Column(unique = false, nullable = false)
	public void setDate(Date date) {
		this.date = date;
	}

	@Column(unique = false, nullable = false)
	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setWord(String word) {
		this.word = word;
	}

	@Override
	public String toString() {
		// XXX
		return null;
	}
}

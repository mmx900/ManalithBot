package models.plugin.dictionary;

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
public class Word {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Integer id;

	@Column(unique = false, nullable = false)
	private String word;

	@Column(unique = false, nullable = false)
	private String description;

	@Column(unique = false, nullable = false)
	private Date date;

	@Column(unique = false, nullable = false)
	private String author;

	public String getAuthor() {
		return author;
	}

	public Date getDate() {
		return date;
	}

	public String getDescription() {
		return description;
	}

	public Integer getId() {
		return id;
	}

	public String getWord() {
		return word;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setDate(Date date) {
		this.date = date;
	}

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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return "'" + author + "','" + sdf.format(date.getTime()) + "','"
				+ description + "','" + word + "'";
	}
}

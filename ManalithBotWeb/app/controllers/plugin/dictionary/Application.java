package controllers.plugin.dictionary;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import models.plugin.dictionary.Word;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.plugin.dictionary.history;
import views.html.plugin.dictionary.list;
import views.html.plugin.dictionary.view;

public class Application extends Controller {

	@Transactional(readOnly = true)
	public static Result list(String author) {
		List<String> words = null;

		if (StringUtils.isBlank(author)) {
			words = JPA
					.em()
					.createQuery(
							"SELECT DISTINCT w.word FROM Word w ORDER BY w.word")
					.getResultList();
		} else {
			words = JPA
					.em()
					.createQuery(
							"SELECT DISTINCT w.word FROM Word w WHERE w.author=:author ORDER BY w.word")
					.setParameter("author", author).getResultList();
		}
		return ok(list.render(words));
	}

	@Transactional(readOnly = true)
	public static Result view(String word) {
		Query query = JPA.em().createQuery(
				"SELECT w FROM Word w WHERE w.word=:word ORDER BY w.date DESC");
		query.setParameter("word", word);
		Word w = (Word) query.setMaxResults(1).getSingleResult();

		return ok(view.render(w));
	}

	@Transactional(readOnly = true)
	public static Result history(String word) {
		List<Word> words = JPA
				.em()
				.createQuery(
						"SELECT w FROM Word w WHERE w.word=:word ORDER BY w.date DESC")
				.setParameter("word", word).getResultList();
		return ok(history.render(words));
	}
}
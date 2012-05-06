package controllers.plugin.dictionary;

import java.util.List;

import javax.persistence.Query;

import models.plugin.dictionary.Word;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.plugin.dictionary.list;
import views.html.plugin.dictionary.view;

public class Application extends Controller {

	@Transactional(readOnly = true)
	public static Result list() {
		List<String> words = JPA
				.em()
				.createQuery(
						"SELECT DISTINCT w.word FROM Word w ORDER BY w.word")
				.getResultList();
		return ok(list.render(words));
	}

	@Transactional(readOnly = true)
	public static Result view(String word) {
		Query query = JPA.em().createQuery(
				"SELECT w FROM Word w WHERE w.word=:arg1 ORDER BY w.date DESC");
		query.setParameter("arg1", word);
		Word w = (Word) query.getSingleResult();

		return ok(view.render(w));
	}
}
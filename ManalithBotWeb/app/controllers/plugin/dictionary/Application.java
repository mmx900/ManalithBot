package controllers.plugin.dictionary;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Query;

import models.plugin.dictionary.Word;
import name.fraser.neil.plaintext.diff_match_patch;
import name.fraser.neil.plaintext.diff_match_patch.Diff;

import org.apache.commons.lang.StringUtils;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.plugin.dictionary.history;
import views.html.plugin.dictionary.list;
import views.html.plugin.dictionary.show;
import views.html.plugin.dictionary.diff;

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
	public static Result show(String word) {
		Query query = JPA.em().createQuery(
				"SELECT w FROM Word w WHERE w.word=:word ORDER BY w.date DESC");
		query.setParameter("word", word);
		Word w = (Word) query.setMaxResults(1).getSingleResult();

		return ok(show.render(w));
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

	@Transactional(readOnly = true)
	public static Result diff(String word, Long revision1, Long revision2) {
		List<Word> words = JPA
				.em()
				.createQuery(
						"SELECT w FROM Word w WHERE w.word=:word AND (w.id=:revision1 OR w.id=:revision2) ORDER BY w.date DESC")
				.setParameter("word", word)
				.setParameter("revision1", revision1)
				.setParameter("revision2", revision2).getResultList();

		// TODO 예외처리
		// if (words.size() != 2)
		// return error()

		Word word1 = words.get(0);
		Word word2 = words.get(1);

		diff_match_patch dmp = new diff_match_patch();

		LinkedList<Diff> diffs = dmp.diff_main(word1.getDescription(),
				word2.getDescription());

		return ok(diff.render(words, diffs));
	}
}
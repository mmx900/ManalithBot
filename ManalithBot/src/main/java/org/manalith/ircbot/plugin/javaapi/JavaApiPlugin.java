package org.manalith.ircbot.plugin.javaapi;

import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

@Component
public class JavaApiPlugin extends SimplePlugin {

	private static final String NAMESPACE = "!japi";

	@Override
	public String getName() {
		return "Java API Viewer";
	}

	@Override
	public String getCommands() {
		return NAMESPACE;
	}

	@Override
	public String getHelp() {
		return "설  명: 지정한 클래스 이름에 대한 자바문서 연결을 보여줍니다, 사용법: !japi [클래스명]";
	}

	@Override
	public void onMessage(MessageEvent event) {
		String message = event.getMessage();

		if (message.equals(NAMESPACE + ":help"))
			event.respond(getHelp());
		else if (message.length() >= 8
				&& message.substring(0, 8).equals("java-api")) {
			event.respond(getApiDocument(message));
		}
	}

	private String getApiDocument(String query) {
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = new StringTokenizer(query, " ");
		st.nextToken();
		if (st.countTokens() == 0 || st.countTokens() > 2) {
			sb.append("사용법 :  java-api [KEYWORD] [SOURCE]");
		} else if (st.countTokens() == 1) {
			sb.append(JavaDocSearchFactory.getSearch(
					JavaDocSources.getSourceURL("j2se")).search(st.nextToken()));
		} else if (st.countTokens() == 2) {
			String keyword = st.nextToken();
			String sourceName = st.nextToken();
			SearchInterface searcher = null;
			if (sourceName.equals("search")) {
				// TODO search 기능 만들기
				// FIXME 미완성 - String을 리턴하는 함수 자체의 구조적 한계
				for (String sourceURL : JavaDocSources.getSources().values()) {
					searcher = JavaDocSearchFactory.getSearch(sourceURL);
				}
			} else if (JavaDocSources.hasSource(sourceName)) {
				searcher = JavaDocSearchFactory.getSearch(JavaDocSources
						.getSourceURL(sourceName));
				sb.append(searcher.search(keyword));

				if (StringUtils.isBlank(sb.toString())) {
					sb.append("[");
					sb.append(query);
					sb.append("]");
					sb.append(" 을(를) 찾을 수 없습니다.");
				}
			} else {
				sb.append("현재 등록된 소스 : ");
				sb.append(JavaDocSources.getSourceNamesByString());
			}
		}
		return sb.toString();
	}
}

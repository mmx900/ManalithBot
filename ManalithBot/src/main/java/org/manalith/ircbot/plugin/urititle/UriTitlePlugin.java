package org.manalith.ircbot.plugin.urititle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;

public class UriTitlePlugin extends AbstractBotPlugin {
    public UriTitlePlugin(ManalithBot bot) {
        super(bot);
    }

    public String getNamespace() {
        return null;
    }

    public String getName() {
        return "URI 타이틀";
    }

    public String getHelp() {
        return "대화 중 등장하는 URI의 타이틀을 표시합니다";
    }

    private String findUri(String msg) {
        if (!msg.contains("http"))
            return null;

        String URI_REGEX = ".*(https?://\\S+).*";
        Pattern pattern = Pattern.compile(URI_REGEX);
        Matcher matcher = pattern.matcher(msg);

        if (!matcher.matches())
            return null;

        return matcher.group(1);
    }

    private String getTitle(String uri) {
        try {
            Document doc = Jsoup.connect(uri).get();
            return doc.title();
        } catch (Exception e) {
            return null;
        }
    }

    public void onMessage(MessageEvent event) {
        String message = event.getMessage();
        String channel = event.getChannel();

        String uri = findUri(message);
        if (uri == null)
            return;

        String title = getTitle(uri);
        if (title != null) {
            bot.sendLoggedMessage(channel, "[Link Title] " + title);;
        }
        event.setExecuted(true);
    }
}

package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {

    private DateTimeParser parser;

    public SqlRuParse(DateTimeParser parser) {
        this.parser = parser;
    }

    @Override
    public List<Post> list(String link) throws IOException {
        List<Post> posts = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            Document doc = Jsoup.connect(link + i).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                if (!td.child(0).text().toLowerCase().contains("javascript")
                        && td.child(0).text().contains("Java")) {
                    posts.add(detail(td.child(0).attr("href")));
                }
            }
        }
        return posts;
    }

    @Override
    public Post detail(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        Elements row = doc.select(".msgBody");
        String description = row.get(1).text();
        row = doc.select(".msgFooter");
        String date = row.first().text().substring(0, row.get(0).text().indexOf(" ["));
        String name = doc.select(".messageHeader").get(0).ownText();
        return new Post(name, link, description, parser.parse(date));
    }
}

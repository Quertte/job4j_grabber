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
    public List<Post> list(String link) {
        List<Post> posts = new ArrayList<>();
        try {
            for (int i = 1; i < 6; i++) {
                Document doc = Jsoup.connect(link + i).get();
                Elements row = doc.select(".postslisttopic");
                for (Element td : row) {
                    if (!td.child(0).text().toLowerCase().contains("javascript")
                            && td.child(0).text().toLowerCase().contains("java")) {
                        posts.add(detail(td.child(0).attr("href")));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post detail(String link) {
        Document doc;
        Elements row;
        String description = null;
        String date = null;
        String name = null;
        try {
            doc = Jsoup.connect(link).get();
            row = doc.select(".msgBody");
            description = row.get(1).text();
            row = doc.select(".msgFooter");
            date = row.first().text().substring(0, row.get(0).text().indexOf(" ["));
            name = doc.select(".messageHeader").get(0).ownText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Post(name, link, description, parser.parse(date));
    }
}

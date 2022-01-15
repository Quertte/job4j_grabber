package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;

public class SqlRuParse {
    public static void main(String[] args) throws IOException {
        for (int i = 1; i < 6; i++) {
            Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers/" + i).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element parent = td.parent();
                System.out.println(td.child(0).text());
                System.out.println(td.child(0).attr("href"));
                System.out.println(parent.child(5).text());
            }
        }
    }

    private static Post detailPost(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements row = doc.select(".msgBody");
        String description = row.get(1).text();
        row = doc.select(".msgFooter");
        String date = row.first().text().substring(0, row.get(0).text().indexOf(" ["));
        row = doc.select(".messageHeader");
        String name = row.first().text().substring(0, row.get(1).text().length() - 9);
        return new Post(name, url, description, new SqlRuDateTimeParser().parse(date));
    }

}

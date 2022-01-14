package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
        System.out.println(description(
                "https://www.sql.ru/forum/1341435/postgresql-developer-middle-300-net"));
    }

    private static String description(String url) throws IOException {
        String description = null;
        Document doc = Jsoup.connect(url).get();
        Elements row = doc.select(".msgBody");
        for (Element td : row) {
            Element parent = td.parent();
            description = parent.child(1).text();
            break;
        }
        row = doc.select(".msgFooter");
        for (Element td : row) {
            description = description + "/" + td.text().substring(0, td.text().indexOf("["));
            break;
        }
        return description;
    }
}

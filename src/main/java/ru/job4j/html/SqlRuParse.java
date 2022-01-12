package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;

public class SqlRuParse {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
/*
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            Element href = td.child(0);
            System.out.println(href.attr("href"));
            System.out.println(href.text());
        }
*/
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            Element parent = td.parent();
            System.out.println(td.child(0).text());
            System.out.println(td.child(0).attr("href"));
            System.out.println(parent.child(5).text());
        }
    }
}
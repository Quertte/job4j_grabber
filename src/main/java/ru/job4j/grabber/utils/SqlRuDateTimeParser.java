package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("d MMMM yy HH:mm");

    private static final Map<String, String> MONTHS = Map.ofEntries(
            Map.entry("янв", "января"),
            Map.entry("фев", "февраля"),
            Map.entry("мар", "марта"),
            Map.entry("апр", "апреля"),
            Map.entry("май", "мая"),
            Map.entry("июн", "июня"),
            Map.entry("июл", "июля"),
            Map.entry("авг", "августа"),
            Map.entry("сен", "сентября"),
            Map.entry("окт", "октября"),
            Map.entry("ноя", "ноября"),
            Map.entry("дек", "декабря")
    );

    @Override
    public LocalDateTime parse(String parse) {
        LocalDateTime date;
        if (parse.startsWith("сегодня")) {
            date = LocalDateTime.now();
        } else if (parse.startsWith("вчера")) {
            date = LocalDateTime.now().minusDays(1);
        } else {
            var dateTime = parse.split(",");
            var month = dateTime[0].split(" ");
            var localDateTime = month[0] + " " + MONTHS.get(month[1]) + " " + month[2]
                    +  dateTime[1];
            date = LocalDateTime.parse(localDateTime, FORMATTER);
        }
        return date;
    }
}

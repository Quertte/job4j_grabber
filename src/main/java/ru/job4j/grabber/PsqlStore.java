package ru.job4j.grabber;

import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;
import ru.job4j.quartz.AlertRabbit;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private Connection on;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
            on = DriverManager.getConnection(
                    cfg.getProperty("jdbc.url"),
                    cfg.getProperty("jdbc.username"),
                    cfg.getProperty("jdbc.password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static Properties load() {
        Properties config = new Properties();
        try (InputStream in =
                     AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            config.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }

    @Override
    public void close() throws Exception {
        if (on != null) {
            on.close();
        }
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement = on.prepareStatement(
                "insert into post(name, text, link, created) values (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getDescription());
            statement.setString(3, post.getLink());
            statement.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            statement.execute();
            try (ResultSet genKeys = statement.getGeneratedKeys()) {
                if (genKeys.next()) {
                    post.setId(genKeys.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement statement = on.prepareStatement("select * from post")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    posts.add(find(resultSet));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post findById(int id) {
        Post post = null;
        try (PreparedStatement statement = on.prepareStatement("select * from post where id = ?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    post = find(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    private static Post find(ResultSet resultSet) throws SQLException {
        return new Post(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("text"),
                resultSet.getString("link"),
                resultSet.getTimestamp("created").toLocalDateTime()
        );
    }

    public static void main(String[] args) {
        DateTimeParser parser = new SqlRuDateTimeParser();
        Parse parse = new SqlRuParse(parser);
        List<Post> posts = parse.list("https://www.sql.ru/forum/job-offers/");
        Properties properties = load();
        try (PsqlStore store = new PsqlStore(properties)) {
            for (Post post : posts) {
                store.save(post);
            }
            System.out.println(store.findById(3).getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

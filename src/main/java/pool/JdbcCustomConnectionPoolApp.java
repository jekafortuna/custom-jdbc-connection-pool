package pool;

import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.Connection;

public class JdbcCustomConnectionPoolApp {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";
    private static DataSource dataSource;

    @SneakyThrows
    public static void main(String[] args) {
        initializeJdbcCustomPonnectionPool();

        var start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            try (Connection connection = dataSource.getConnection()) {

            }
        }
        var end = System.nanoTime();
        System.out.println("It took: " + (end - start) / 1_000_000 + " millis to get 1000 connections");
    }

    private static void initializeJdbcCustomPonnectionPool() {
        ProxyDataSource proxyDataSource = new ProxyDataSource(URL, USERNAME, PASSWORD);
        dataSource = proxyDataSource;
    }
}

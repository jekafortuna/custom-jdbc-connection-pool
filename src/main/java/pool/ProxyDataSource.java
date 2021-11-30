package pool;

import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ProxyDataSource extends PGSimpleDataSource {
    private static final int DEFAULT_POOL_SIZE = 10;
    private Queue<Connection> connectionPool = new ConcurrentLinkedDeque<>();

    public ProxyDataSource(String url, String username, String password) {
        super();
        init(url, username, password);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connectionPool.poll();
    }

    @SneakyThrows
    private void init(String url, String username, String password) {
        setURL(url);
        setUser(username);
        setPassword(password);
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            try (Connection physicalConnection = super.getConnection()) {
                ProxyConnection proxyConnection = new ProxyConnection(physicalConnection, connectionPool);
                connectionPool.add(proxyConnection);
            }
        }
    }
}

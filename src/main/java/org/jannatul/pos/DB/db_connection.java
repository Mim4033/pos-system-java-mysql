package org.jannatul.pos.DB;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class db_connection {
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/pos_system"); //localhost connection database "pos_system"
        config.setUsername("root"); //database user
        config.setPassword("Runa4033"); //database password

        dataSource = new HikariDataSource(config);
    }
    public db_connection() {}

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

}

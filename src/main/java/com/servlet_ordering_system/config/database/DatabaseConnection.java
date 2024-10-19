package com.servlet_ordering_system.config.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/ordering_system";
    private static final String USER = System.getenv("MYSQL_USER");
    private static final String PASSWORD = System.getenv("MYSQL_PASSWORD");

    static {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public static void closeConnection(Connection connection) {
        try {
            Objects.requireNonNull(connection).close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void setAutoCommit(Connection conn, boolean value) {
        try {
            conn.setAutoCommit(value);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void commit(Connection conn) {
        try {
            conn.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void rollback(Connection conn) {
        try {
            conn.rollback();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

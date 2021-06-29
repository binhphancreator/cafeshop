package core.database;

import core.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class ConnectorMySQL {
    private String DB_URL;
    private String USER_NAME;
    private String PASSWORD;

    public ConnectorMySQL(Environment env) {
        this.DB_URL = "jdbc:mysql://" + env.env("DB_HOST") + ":" + env.env("DB_PORT") + "/" + env.env("DB_DATABASE");
        this.USER_NAME = env.env("DB_USERNAME");
        this.PASSWORD = env.env("DB_PASSWORD");
    }

    public Connection getConnectionSQL() {
        try {
            return DriverManager.getConnection(this.DB_URL, this.USER_NAME, this.PASSWORD);
        } catch (SQLException e) {
            return null;
        }
    }

    public Statement getStatement(){
        try {
            return this.getConnectionSQL().createStatement();
        } catch (SQLException e) {
            return null;
        }
    }
}

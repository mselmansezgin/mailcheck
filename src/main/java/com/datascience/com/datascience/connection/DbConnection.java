package com.datascience.com.datascience.connection;

import com.datascience.AppProperties;

import java.sql.Connection;
import java.sql.SQLException;

public interface DbConnection {
    public DbConnection getConnection(AppProperties props) throws SQLException, ClassNotFoundException;
}

package com.datascience.com.datascience.dboperations;

import java.sql.SQLException;

public interface DbOperation {

    public void call() throws SQLException, ClassNotFoundException;
}

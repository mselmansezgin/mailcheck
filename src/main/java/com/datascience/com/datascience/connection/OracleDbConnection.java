package com.datascience.com.datascience.connection;

import com.datascience.AppProperties;
import com.datascience.com.datascience.schedule.ScheduledTasks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleDbConnection {


    public static final Logger log = LoggerFactory.getLogger(OracleDbConnection.class);
    private AppProperties props;
    private static Connection CON_INSTANCE = null;

    public OracleDbConnection(){

    }

    public OracleDbConnection(AppProperties props) throws ClassNotFoundException, SQLException {
        this.props = props;
    }

    public static Connection getConnection(AppProperties props) throws SQLException, ClassNotFoundException {
        if (CON_INSTANCE == null) {
            log.info("Connecting to database");
            Class.forName("oracle.jdbc.driver.OracleDriver");
            CON_INSTANCE = DriverManager.getConnection(props.getDbUrl(), props.getDbUsername(), props.getDbPassword());
            log.info("Connected to database");
        }
        return CON_INSTANCE;
    }

    @Autowired
    public void setProps(AppProperties props){
        this.props  = props;
    }
}

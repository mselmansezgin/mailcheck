package com.datascience.com.datascience.dboperations;


import com.datascience.com.datascience.connection.OracleDbConnection;
import com.datascience.com.datascience.schedule.ScheduledTasks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class OracleDbUserOperation implements DbOperation {

    public final Logger log = LoggerFactory.getLogger(OracleDbUserOperation.class);
    private Connection oraCon;

    public OracleDbUserOperation(Connection oraCon){
        this.oraCon = oraCon;
    }

    @Override
    public void call() throws SQLException, ClassNotFoundException {

        //(p_code varchar2, p_value number default null,p_out out varchar2)
        String command = "{call HD_LIVE.upd_opq_dc(?,?,?)}";
        CallableStatement cstmt = oraCon.prepareCall(command);
        cstmt.registerOutParameter(3, Types.VARCHAR);

        cstmt.setString(1, "USER");
        cstmt.setString(2, null);
        cstmt.execute();
        String str = cstmt.getString(3);

        cstmt.close();
        System.out.println("Retorno: " + str);

    }



}

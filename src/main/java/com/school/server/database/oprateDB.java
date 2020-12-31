package com.school.server.database;
import com.school.server.JavaTcpMemory;
import com.school.server.tool.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.apache.iotdb.jdbc.IoTDBSQLException;

import javax.swing.*;
import java.sql.*;

/**
 * @author test
 */
@Getter
@Setter
@Log4j
public class oprateDB {
    private Connection connection = null;
    private Statement statement = null;
    public oprateDB(boolean debug){
        try {
            Class.forName(JavaTcpMemory.driver);
            this.connection=DriverManager.getConnection(JavaTcpMemory.jdbc, JavaTcpMemory.username, JavaTcpMemory.password);
        }catch (SQLException ex){
            log.error("SQLException: " + ex.getMessage());
            log.error("SQLState: " + ex.getSQLState());
            log.error("VendorError: " + ex.getErrorCode());
        }catch (ClassNotFoundException e) {
            log.error(e.toString());
        }
    }
    public ResultSet doSQL(String sql){
        try{
            this.statement = this.connection.createStatement();
            this.statement.execute(sql);
            log.info(statement.getResultSet().getMetaData());
            return  statement.getResultSet();
        }catch (SQLException ex){
            log.error("SQLException: " + ex.getMessage());
            log.error("SQLState: " + ex.getSQLState());
            log.error("VendorError: " + ex.getErrorCode());
            return null;
        }
    }
    public static String makeSQL(byte[] line,byte[] car,byte[] cmd,String startDate,String endDate){
        String lineS=new String(line);
        String carS=new String(car);
        String cmdS=new String(cmd);
        return String.format("select OriginalPackage from root.%s.%s.%s where time>%s and time<%s",lineS,carS,cmdS,startDate,endDate);
    }
}

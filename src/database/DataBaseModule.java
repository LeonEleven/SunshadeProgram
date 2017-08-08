package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bhL on 2017/2/15.
 */
public class DataBaseModule {

    Connection conn;
    PreparedStatement ps;
    ResultSet rs;

    public DataBaseModule() {
        String driver = "com.mysql.jdbc.Driver";
        String dbName = "sadb";
        String userName = "root";
        String password = "mysqlbhl";
        String url = "jdbc:mysql://localhost:3306/" + dbName + "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.out.println("Error Loading JDBC Driver.");
        }
        try {
            conn = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            System.out.println("Error Connecting SQL.");
        }
    }

    public List<Map<String, Object>> getSqlQueryResToList(String sql, Object... params) {

        List<Map<String, Object>> rows = new ArrayList<>();
        if (null == sql || "".equals(sql)) {
            return new ArrayList<>();
        }
        try {
            ps = conn.prepareStatement(sql);
            if (null != params && params.length > 0) {
                for (int i = 0, len = params.length; i < len; i++) {
                    Object arg = params[i];
                    ps.setObject(i + 1, arg);
                }
            }
            rs = ps.executeQuery();
            if (null != rs) {
                ResultSetMetaData rsm = rs.getMetaData();
                int count = rsm.getColumnCount();
                Map<String, Object> record;
                if (count > 0) {
                    while (rs.next()) {
                        record = new HashMap<>();
                        for (int j = 0; j < count; j++) {
                            Object obj = rs.getObject(j + 1);
                            String columnName = rsm.getColumnName(j + 1);
                            record.put(columnName, (obj == null) ? "" : obj);
                        }
                        rows.add(record);
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }

    public void release() {
        try {
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
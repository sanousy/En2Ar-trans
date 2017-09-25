/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package En2ArTrans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Sanousy
 */
public class database
{

    private String dbName = "null.db";
//    Connection c = null;
//    Statement stmt = null;

    database(String name)
    {
        dbName = "jdbc:sqlite:" + name;

        try {
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection(dbName);
            c.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            //System.exit(0);
        }
    }

//    public void closedb()
//    {
//        try {
//            c.close();
//        } catch (SQLException ex) {
//            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    void createTable(String sql)
    {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection(dbName);
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            // System.exit(0);
        }
    }

    void exec(String sql)
    {

        try {
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection(dbName);
            c.setAutoCommit(false);
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(sql);
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            //  System.exit(0);
        }
    }

    String[][] execQuery(String Columns, String Table, String Condition)
    {
        Connection c = null;
        Statement stmt = null;
        ResultSet rs = null;
        String[][] list = null;
        String sql = "Select " + Columns + "  from " + Table + " where " + Condition;
        String countSql = "Select count(1) as count  from " + Table + " where " + Condition;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(dbName);
            c.setAutoCommit(false);
          //  System.out.println("Opened database successfully");

            stmt = c.createStatement();
            rs = stmt.executeQuery(countSql);
            int rows = rs.getInt("count");

            if (rows > 0) {
                rs = stmt.executeQuery(sql);
                ResultSetMetaData md = rs.getMetaData();
                int columns = md.getColumnCount();

                list = new String[rows][columns];
                int row = 0;
                while (rs.next()) {
                    for (int col = 0; col < columns; col++) {
                        list[row][col] = rs.getString(rs.getMetaData().getColumnName(col + 1));
                    }
                    row++;
                }
            }

            rs.close();

            stmt.close();

            c.close();

        } catch (Exception e) {
            System.err.println(sql);
            System.err.println(countSql);
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            // System.exit(0);
        }
        return list;
    }

}

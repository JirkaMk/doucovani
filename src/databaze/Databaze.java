/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaze;

import java.sql.*;

/**
 *
 * @author jirka_000
 */
public class Databaze {

   
    public static Connection getConnection() throws Exception {
        try {
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://sql11.freesqldatabase.com:3306/sql11217211";
            String username = "sql11217211";
            String password = "css3pSfev7";
            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected");
            return conn;
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }
    
    public static Connection post() throws Exception {
        final String var1 = "ZAM1";
        final String var2 = "Jiri";
        final String var3 = "Test";
        
        try {
            Connection con = getConnection();
            PreparedStatement posted  = con.prepareCall("INSERT INTO ZAKAZNIK(ZAK_ID,PRIJMENI,JMENO) VALUES('"+var1+"','"+var3+"','"+var2+"')");
          
            posted.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
        finally {
            System.out.println("Insert completed.");
        }

        return null;
    }   

}

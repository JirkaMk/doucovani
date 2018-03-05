/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userInterface.kurz;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;
import userInterface.dite.*;
import userInterface.zakaznik.*;

/**
 *
 * @author jirka_000
 */
public class PolozkaKurz {
    private String polozka;
    private String hodnota;

    public PolozkaKurz() {
    }       
    
    public PolozkaKurz(String polozka, String hodnota) {
        this.polozka = polozka;
        this.hodnota = hodnota;
    }   
   
    public String getPolozka() {
        return polozka;
    }

    public void setPolozka(String polozka) {
        this.polozka = polozka;
    }

    public String getHodnota() {
        return hodnota;
    }

    public void setHodnota(String hodnota) {
        this.hodnota = hodnota;
    }
    
    public int jeKurzPlny(int kurzID) throws Exception  {

        Statement stmt = null;
        int kapacita = -1;
        int prihlaseno = -2;
        
        String query = "SELECT KAPACITA,POCET_PRIHLASEK FROM KURZ WHERE KURZ_ID ='" + kurzID + "';";
        try {
            Connection con = databaze.Databaze.getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                 kapacita = rs.getInt(1);
                 prihlaseno = rs.getInt(2);
                 
                 if(kapacita==prihlaseno){
                     return 1;
                 }
                 else return 2;
            }
        } catch (SQLException e) {
            System.out.println(e);
            return 3;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return 3;
    }
        
}

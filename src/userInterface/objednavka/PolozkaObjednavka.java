/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userInterface.objednavka;


/**
 *
 * @author jirka_000
 */
public class PolozkaObjednavka {
    private String polozka;
    private String hodnota;

    public PolozkaObjednavka(String polozka, String hodnota) {
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
    

        
}

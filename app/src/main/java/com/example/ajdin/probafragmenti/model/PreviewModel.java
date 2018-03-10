package com.example.ajdin.probafragmenti.model;

/**
 * Created by ajdin on 9.3.2018..
 */

public class PreviewModel {
    private String naziv;
    private String cijena;
    private String kolicina;

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getCijena() {
        return cijena;
    }

    public void setCijena(String cijena) {
        this.cijena = cijena;
    }

    public String getKolicina() {
        return kolicina;
    }

    public void setKolicina(String kolicina) {
        this.kolicina = kolicina;
    }

    public PreviewModel(String naziv, String cijena, String kolicina) {

        this.naziv = naziv;
        this.cijena = cijena;
        this.kolicina = kolicina;
    }
}

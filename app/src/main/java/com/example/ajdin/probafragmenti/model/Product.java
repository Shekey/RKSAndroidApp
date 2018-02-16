package com.example.ajdin.probafragmenti.model;



import com.example.ajdin.probafragmenti.adapter.Saleable;

import java.io.Serializable;
import java.math.BigDecimal;

public class Product implements Saleable, Serializable {
    private static final long serialVersionUID = -4073256626483275668L;

    private String pId;
    private String pName;
    private BigDecimal pPrice;
    private String Bar_kod;
    private String Stanje;
    private String Jedinica_mjere;

    public String getJedinica_mjere() {
        return Jedinica_mjere;
    }

    public Product() {
        super();
    }
    public Product(Product p){
        this.pId = p.pId;
        this.pName =p. pName;
        this.pPrice = p.pPrice;
        this.Bar_kod = p.Bar_kod;
        this.Stanje = p.Stanje;
        this.Jedinica_mjere = p.Jedinica_mjere;
    }

    public Product(String pId, String pName, BigDecimal pPrice, String bar_kod, String jedinica_mjere, String stanje) {
        this.pId = pId;
        this.pName = pName;
        this.pPrice = pPrice;
        this.Bar_kod = bar_kod;
        this.Stanje = stanje;
        this.Jedinica_mjere = jedinica_mjere;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Product)) return false;

        return (this.pId == ((Product) o).getId());
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 1;
        hash = hash * prime + Integer.valueOf(pId);
        hash = hash * prime + (pName == null ? 0 : pName.hashCode());
        hash = hash * prime + (pPrice == null ? 0 : pPrice.hashCode());


        return hash;
    }

    public String getStanje() {
        return Stanje;
    }

    @Override
    public BigDecimal getPrice() {
        return pPrice;
    }



    @Override
    public String getName() {
        return pName;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public BigDecimal getpPrice() {
        return pPrice;
    }

    public void setpPrice(BigDecimal pPrice) {
        this.pPrice = pPrice;
    }

    public String getBar_kod() {
        return Bar_kod;
    }

    public void setBar_kod(String bar_kod) {
        Bar_kod = bar_kod;
    }

    public void setJedinica_mjere(String jedinica_mjere) {
        Jedinica_mjere = jedinica_mjere;
    }

    public String getId() {
        return pId;

    }
}

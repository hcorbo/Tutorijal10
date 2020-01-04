package ba.unsa.etf.rpr;

import javafx.beans.property.SimpleStringProperty;

public class Drzava {
    private int id;
    private SimpleStringProperty naziv;
    private Grad glavniGrad;

    public Drzava() {
        this.naziv =  new SimpleStringProperty("");
        this.glavniGrad = null;
    }

    public Drzava(int id, String naziv, Grad glavniGrad) {
        this.id = id;
        this.naziv =  new SimpleStringProperty(naziv);
        this.glavniGrad = glavniGrad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Grad getGlavniGrad() {
        return glavniGrad;
    }

    public void setGlavniGrad(Grad glavniGrad) {
        this.glavniGrad = glavniGrad;
    }

    public String getNaziv() {
        return naziv.get();
    }

    public SimpleStringProperty nazivProperty() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv.set(naziv);
    }

    @Override
    public String toString() {
        return naziv.get();
    }
}

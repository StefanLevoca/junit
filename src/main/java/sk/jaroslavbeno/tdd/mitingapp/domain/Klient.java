package sk.jaroslavbeno.tdd.mitingapp.domain;

import java.util.Objects;

public class Klient {
    private String nazov;

    public Klient(String nazov) {
        this.nazov = nazov;
    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Klient klient = (Klient) o;
        return Objects.equals(nazov, klient.nazov);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nazov);
    }
}

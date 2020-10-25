package sk.jaroslavbeno.tdd.mitingapp.domain;

import sk.jaroslavbeno.tdd.mitingapp.domain.stretnutie.TerminStretnutia;

public class Stretnutie {
    private TerminStretnutia terminStretnutia;
    private Klient klient;

    public Stretnutie(TerminStretnutia terminStretnutia, Klient klient) {
        this.terminStretnutia = terminStretnutia;
        this.klient = klient;
    }

    public TerminStretnutia getTerminStretnutia() {
        return terminStretnutia;
    }

    public void setTerminStretnutia(TerminStretnutia terminStretnutia) {
        this.terminStretnutia = terminStretnutia;
    }

    public Klient getKlient() {
        return klient;
    }

    public void setKlient(Klient klient) {
        this.klient = klient;
    }
}

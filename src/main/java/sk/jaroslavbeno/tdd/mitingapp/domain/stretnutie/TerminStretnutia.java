package sk.jaroslavbeno.tdd.mitingapp.domain.stretnutie;

import java.time.Duration;
import java.time.LocalTime;

public class TerminStretnutia {
    private LocalTime cas;
    private Duration dlzka;

    TerminStretnutia(LocalTime cas, Duration dlzka) {
        this.cas = cas;
        this.dlzka = dlzka;
    }

    public LocalTime getCas() {
        return cas;
    }

    public Duration getDlzka() {
        return dlzka;
    }

    public boolean prekryvaSa(TerminStretnutia terminStretnutia){
        LocalTime cas1Zaciatok = this.cas;
        LocalTime cas1Koniec = cas1Zaciatok.plus(this.dlzka);
        LocalTime cas2Zaciatok = terminStretnutia.cas;
        LocalTime cas2Koniec = cas2Zaciatok.plus(terminStretnutia.dlzka);

        return cas1Zaciatok.isBefore(cas2Koniec) && cas2Zaciatok.isBefore(cas1Koniec);
    }
}

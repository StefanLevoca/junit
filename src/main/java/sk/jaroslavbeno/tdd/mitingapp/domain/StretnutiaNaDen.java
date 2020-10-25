package sk.jaroslavbeno.tdd.mitingapp.domain;

import sk.jaroslavbeno.tdd.mitingapp.domain.stretnutie.TerminStretnutia;
import sk.jaroslavbeno.tdd.mitingapp.domain.stretnutie.TerminStretnutiaFactory;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class StretnutiaNaDen {
    private LocalDate den;
    private List<Stretnutie> stretnutia;

    public StretnutiaNaDen(LocalDate den) {
        this.den = den;
        stretnutia = new ArrayList<>();
    }

    public LocalDate getDen() {
        return den;
    }

    public void setDen(LocalDate den) {
        this.den = den;
    }

    public List<Stretnutie> getStretnutia() {
        return stretnutia;
    }

    public void setStretnutia(List<Stretnutie> stretnutia) {
        this.stretnutia = stretnutia;
    }

    public boolean jeVolno(int hodina, int minuta) {
        TerminStretnutia terminStretnutia = TerminStretnutiaFactory.create(hodina, minuta);
        return stretnutia.stream()
                .noneMatch(
                        stretnutie -> stretnutie.getTerminStretnutia().prekryvaSa(terminStretnutia)
                );
    }

    public Klient ktoMaStretnutie(int hodina, int minuta) {

        TerminStretnutia terminStretnutia = TerminStretnutiaFactory.create(hodina, minuta);
        Stretnutie najdeneStretnutie = null;

        for (Stretnutie stretnutie : stretnutia) {
            TerminStretnutia termin = stretnutie.getTerminStretnutia();
            LocalTime koniec = termin.getCas().plus(termin.getDlzka());
            LocalTime zaciatok = termin.getCas();

            if ((koniec.isAfter(terminStretnutia.getCas()))
                    &&
                    (zaciatok.isBefore(terminStretnutia.getCas())
                            || zaciatok.equals(terminStretnutia.getCas()))) {
                najdeneStretnutie = stretnutie;
                break;
            }
        }

        return najdeneStretnutie != null ? najdeneStretnutie.getKlient() : null;

    }

    public TerminStretnutia kedyMaStretnutie(String klientNazov) {

        Stretnutie najdeneStretnutie = null;

        najdeneStretnutie = stretnutia.stream()
                .filter(stretnutie -> stretnutie.getKlient().getNazov().equals(klientNazov)).findFirst().orElse(null);

        return najdeneStretnutie != null ? najdeneStretnutie.getTerminStretnutia() : null;
    }

    /**
     * Umožniť objednať termín stretnutia pre nového klienta na nový termín
     *
     * @param nazov  meno klienta
     * @param hodina hodina terminu
     * @param minuta minuta terminu
     * @return boolean premenna indikujuca ci sa podarila akcia
     */
    public boolean dohodniStretnutieKlienta(String nazov, int hodina, int minuta) {
        TerminStretnutia terminStretnutia = TerminStretnutiaFactory.create(hodina, minuta);
        Klient klient = new Klient(nazov);

        boolean klientUzMaStretnutieTentoDen = stretnutia.stream().anyMatch(stretnutie -> stretnutie.getKlient().equals(klient));
        boolean nastalPrekryvTerminovKlientov = stretnutia.stream().anyMatch(stretnutie -> stretnutie.getTerminStretnutia().prekryvaSa(terminStretnutia));

        return (!klientUzMaStretnutieTentoDen) && (!nastalPrekryvTerminovKlientov);
    }

    /**
     * Umožniť presunúť stretnutie daného klienta na iný čas v ten istý deň
     *
     * @param klient  referencia na objekt klienta
     * @param novyCas cas presunutia isteho stretnutia
     * @return boolean premenna indikujuca ci sa podarila akcia
     */
    public boolean presunStretnutie(Klient klient, LocalTime novyCas) {
        int hodina = novyCas.getHour();
        int minuta = novyCas.getMinute();
        TerminStretnutia novyTermin = TerminStretnutiaFactory.create(hodina, minuta);

        Stretnutie najdeneStretnutieNaPresun = stretnutia.stream().filter(stretnutie -> stretnutie.getKlient().equals(klient)).findFirst().orElse(null);
        if (najdeneStretnutieNaPresun == null) {
            return false;
        } else {
            if (jeVolno(hodina, minuta)) {
                stretnutia.remove(najdeneStretnutieNaPresun);
                stretnutia.add(new Stretnutie(novyTermin, klient));
                return true;
            } else {
                return false;
            }
        }
    }

}

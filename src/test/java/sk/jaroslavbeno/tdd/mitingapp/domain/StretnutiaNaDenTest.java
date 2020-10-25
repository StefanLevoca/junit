package sk.jaroslavbeno.tdd.mitingapp.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import sk.jaroslavbeno.tdd.mitingapp.domain.stretnutie.TerminStretnutia;
import sk.jaroslavbeno.tdd.mitingapp.domain.stretnutie.TerminStretnutiaFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class StretnutiaNaDenTest {

    private LocalDate dnes;
    private StretnutiaNaDen stretnutiaNaDnes;

    @BeforeEach
    void init(){
        dnes = LocalDate.now();
        stretnutiaNaDnes = new StretnutiaNaDen(dnes);
        stretnutiaNaDnes.getStretnutia().add(
                new Stretnutie(TerminStretnutiaFactory.create(10,0), new Klient("Jaroslav Beňo")));
        stretnutiaNaDnes.getStretnutia().add(
                new Stretnutie(TerminStretnutiaFactory.create(11,0), new Klient("PosAm")));
        stretnutiaNaDnes.getStretnutia().add(
                new Stretnutie(TerminStretnutiaFactory.create(11,30), new Klient("Google")));
    }

    @Nested
    class VolnostTerminu{
        @Test
        void termin930JeVolny(){
            assertTrue(stretnutiaNaDnes.jeVolno(9,30));
        }

        @Test
        void termin1000NieJeVolny(){
            assertFalse(stretnutiaNaDnes.jeVolno(10,0));
        }

        @Test
        void termin1059NieJeVolny(){
            assertFalse(stretnutiaNaDnes.jeVolno(10,59));
        }

        @Test
        void termin1030JeVolny(){
            assertTrue(stretnutiaNaDnes.jeVolno(10,30));
        }
    }

    @Nested
    class TestovanieKtoMaStretnutie{

        @Test
        void klientO100JePosAm(){
            assertEquals(stretnutiaNaDnes.ktoMaStretnutie(11,0).getNazov(), "PosAm");
        }

        @Test
        void klientO959JeNik(){
            assertNull(stretnutiaNaDnes.ktoMaStretnutie(9,59));
        }

        @Test
        void klientO1129JePosam(){
            assertEquals(stretnutiaNaDnes.ktoMaStretnutie(11,29).getNazov(), "PosAm");
        }

        @Test
        void klientO1130JeGoogle(){
            assertEquals(stretnutiaNaDnes.ktoMaStretnutie(11,30).getNazov(), "Google");
        }
    }

    @Nested
    class TestovanieKedyMaTermin{

        @Test
        void klientPosAmMaStretnutieO1100(){
            TerminStretnutia terminStretnutia = TerminStretnutiaFactory.create(11,0);
            assertEquals(terminStretnutia.getCas(), stretnutiaNaDnes.kedyMaStretnutie("PosAm").getCas());
        }

        @Test
        void klientYouTubeNemaStretnutie(){
            assertNull(stretnutiaNaDnes.kedyMaStretnutie("YouTube"));
        }

        @Test
        void klientGoogleStretnutieO1130(){
            TerminStretnutia terminStretnutia = TerminStretnutiaFactory.create(11,30);
            assertEquals(terminStretnutia.getCas(), stretnutiaNaDnes.kedyMaStretnutie("Google").getCas());
        }


    }

    @Nested
    class TestovanieObjednatTerminStretnutiaPreNovehoKlientaNaNovyTermin{

        @Test
        @DisplayName("Viac terminov pre IBM")
        void klientMaStretnutieVRovnakyDenNaDvaRozneTerminy(){
            String nazov = "IBM";
            int hodina1 = 13,  minuta1 = 30, hodina2 = 15, minuta2 = 0;
            stretnutiaNaDnes.getStretnutia().add(
                    new Stretnutie(TerminStretnutiaFactory.create(hodina1,minuta1), new Klient(nazov)));
            // pre IBM mame uz jeden termin o 13:30, dalsi v ten den uz nemozeme mat
            assertFalse(stretnutiaNaDnes.dohodniStretnutieKlienta(nazov, hodina2, minuta2));
        }

        @Test
        @DisplayName("Prekryv terminov pre IBM")
        void prekryvTerminovKlientov(){
            // termin koliduje pre IBM s terminom pre Google
            String nazov = "IBM";
            int hodina = 11, minuta = 45;
            assertFalse(stretnutiaNaDnes.dohodniStretnutieKlienta(nazov, hodina, minuta));
        }


        @Test
        @DisplayName("Viac terminov pre IBM aj prekryv terminov")
        void klientMaStretnutieVRovnakyDenNaDvaRozneTerminyAPrekryverminovKlientov() {
            String nazov = "IBM";
            int hodina1 = 13, minuta1 = 30, hodina2 = 11, minuta2 = 40;
            stretnutiaNaDnes.getStretnutia().add(
                    new Stretnutie(TerminStretnutiaFactory.create(hodina1, minuta1), new Klient(nazov)));
            // pre IBM mame uz jeden termin o 13:30, dalsi v ten den uz nemozeme mat a navyse mame aj prekryv terminov s Google
            assertFalse(stretnutiaNaDnes.dohodniStretnutieKlienta(nazov, hodina2, minuta2));
        }

        @Test
        @DisplayName("Ziaden prekryv ani viac terminov")
        void dohodniStretnutieTest(){
            String nazov = "AT&T";
            int hodina = 7, minuta = 0;
            assertTrue(stretnutiaNaDnes.dohodniStretnutieKlienta(nazov, hodina, minuta));
        }

    }



    @Nested
    class TestovaniePresunuStretnutiaDanehoKlientaNaInyCasVTenIstyDen {

        @Test
        @DisplayName("Presun terminu")
        void presunCasuStretnutiaKlienta() {
            Klient klient = new Klient("PosAm");
            LocalTime novyCas = LocalTime.of(17, 0);
            boolean jePresun = stretnutiaNaDnes.presunStretnutie(klient, novyCas);
            assertTrue(jePresun);

            int pocetRovnakych = (int) stretnutiaNaDnes.getStretnutia().stream().filter(stretnutie -> stretnutie.getKlient().equals(klient)).count();

            assertEquals(1, pocetRovnakych);
        }

        @Test
        @DisplayName("Ziaden existujuci termin")
        void ziadenPresunCasuStretnutiaKlienta1() {
            Klient klient = new Klient("Amazon");
            LocalTime novyCas = LocalTime.of(17, 0);
            int velkostZoznamuPred = stretnutiaNaDnes.getStretnutia().size();
            boolean jePresun = stretnutiaNaDnes.presunStretnutie(klient, novyCas);
            assertFalse(jePresun);
            int velkostZoznamuPo = stretnutiaNaDnes.getStretnutia().size();

            assertEquals(velkostZoznamuPred, velkostZoznamuPo);
        }

        @Test
        @DisplayName("Nie je volny termin")
        void ziadenPresunCasuStretnutiaKlienta2() {
            Klient klient = new Klient("PosAm");
            LocalTime novyCas = LocalTime.of(10, 15);
            int velkostZoznamuPred = stretnutiaNaDnes.getStretnutia().size();
            boolean jePresun = stretnutiaNaDnes.presunStretnutie(klient, novyCas);
            assertFalse(jePresun);
            int velkostZoznamuPo = stretnutiaNaDnes.getStretnutia().size();

            assertEquals(velkostZoznamuPred, velkostZoznamuPo);
        }
    }
}
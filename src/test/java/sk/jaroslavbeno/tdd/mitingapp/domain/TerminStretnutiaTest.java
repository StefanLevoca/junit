package sk.jaroslavbeno.tdd.mitingapp.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.jaroslavbeno.tdd.mitingapp.domain.stretnutie.TerminStretnutia;
import sk.jaroslavbeno.tdd.mitingapp.domain.stretnutie.TerminStretnutiaFactory;

import static org.junit.jupiter.api.Assertions.*;

class TerminStretnutiaTest {

    private TerminStretnutia termin1030;
    private TerminStretnutia termin1000;
    private TerminStretnutia termin1029;
    private TerminStretnutia termin1050;
    private TerminStretnutia termin1100;

    @BeforeEach
    void init(){
        termin1030 = TerminStretnutiaFactory.create(10,30);
        termin1029 = TerminStretnutiaFactory.create(10,29);
        termin1000 = TerminStretnutiaFactory.create(10,00);
        termin1050 = TerminStretnutiaFactory.create(10,50);
        termin1100 = TerminStretnutiaFactory.create(11,00);
    }

    @Test
    void termin1030SaPrekryvaSTerminom1050(){
        assertTrue(termin1030.prekryvaSa(termin1050));
    }

    @Test
    void termin1030SaNeprekryvaSTerminom1100(){
        assertFalse(termin1030.prekryvaSa(termin1100));
        assertFalse(termin1100.prekryvaSa(termin1030));
    }

    @Test
    void termin1000SaPrekryvaSTerminom1029(){
        assertTrue(termin1000.prekryvaSa(termin1029));
        assertTrue(termin1029.prekryvaSa(termin1000));
    }


    @Test
    void termin1029SaNeprekryvaSTerminom1100(){
        assertFalse(termin1029.prekryvaSa(termin1100));
        assertFalse(termin1100.prekryvaSa(termin1029));
    }



}
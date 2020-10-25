package sk.jaroslavbeno.junit;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class UtilsTest {

    public static final String MATEMATIKA = "Matematika";
    private Utils utils;

    @BeforeEach
    void beforeMethods(){
        System.out.println("pred metodou");
        utils = new Utils();
    }

    @AfterEach
    void afterMethod(){
        System.out.println("po metode");
    }

    @BeforeAll
    static void beforeAllMethod(){
        System.out.println("Spúšťam beforeAll metódu");
    }

    @AfterAll
    static void afterAllMethod(){
        System.out.println("Spúšťam afterAllMethod metódu");
    }


    @Test
    @DisplayName("Test, ktory vzdy prejde.")
    void passedTest(){
        assertTrue(true);
    }

    @Test
    void sumIs2(){
        int expectedValue = 2;
        int actualValue = utils.sum(1,1);
        assertEquals(expectedValue, actualValue,"Sumovaním očakávam výsledok 2.");
    }

    @Nested
    class PripocitavanieDaneTest{
        @Test
        void suma100PlusDanJe120(){
            assumeTrue(isServerDostupny());
            assertEquals(BigDecimal.valueOf(120L),
                    utils.pripocitajDan(BigDecimal.valueOf(100L)));
        }

        private boolean isServerDostupny() {
            return true;
        }

        @Test
        public void suma141PlusDanJe169Cela2(){
            assertEquals(BigDecimal.valueOf(169.20).setScale(2),
                    utils.pripocitajDan(BigDecimal.valueOf(141L)));
        }

        @RepeatedTest(5)
        public void suma10PlusDanNieJe100(RepetitionInfo repetitionInfo){
            int curentRep = repetitionInfo.getCurrentRepetition();
            int totalRep = repetitionInfo.getTotalRepetitions();
            System.out.println(curentRep +"/"+totalRep);
            assertNotEquals(BigDecimal.valueOf(100).setScale(2),
                    utils.pripocitajDan(BigDecimal.valueOf(10).setScale(2)), "Suma 10 + dan nie je 100");
        }
    }

    @Nested
    class CestanaVOSTest{
        @Test
        @EnabledOnOs(OS.WINDOWS)
        @EnabledOnJre(JRE.JAVA_8)
        void overCestuNaWinSubor(){
            System.out.println("overujem cestu pre Windows");
        }

        @Test
        @EnabledOnOs(OS.LINUX)
        void overCestuNaLinuxSubor(){
            System.out.println("overujem cestu pre Linux");
        }

    }




    @Test
    void obsahTrojuholnikaJe3priDlzkeStrany2AVyskeNaStranu3(){
        assertEquals(3, utils.obsahTrojuholnika(2,3), "Metoda ma vypocitat obsah trojuholnika.");
    }

    @Test
    void obsahTrojuholnikaJe10priDlzkeStrany4AVyskeNaStranu5(){
        assertEquals(10, utils.obsahTrojuholnika(4,5), "Metoda ma vypocitat obsah trojuholnika.");
    }

    @Test
    @Tag(MATEMATIKA)
    void vysledokDelenia10a2je5(){
        assertEquals(5, utils.delenie(10,2));
    }

    @Test
    @Tag(MATEMATIKA)
    void testScitovanie(){
        assertAll(
                ()->assertEquals(30, utils.sum(10,20)),
                ()->assertNotEquals(40, utils.sum(10,20)),
                ()->assertEquals(-2, utils.sum(-1,-1)),
                ()->assertEquals(0, utils.sum(-10,10))
        );
    }


    @Test
    @Tag(MATEMATIKA)
    void delenie0HadzeVynimku(){
        assertThrows(ArithmeticException.class,
                () -> utils.delenie(10,0),
                "Pri deleni 0 musi byt vynimka.");
    }

    @Test
    void nemozeNastatVynimka(){
        assertDoesNotThrow(() -> utils.workWithString());
    }
}
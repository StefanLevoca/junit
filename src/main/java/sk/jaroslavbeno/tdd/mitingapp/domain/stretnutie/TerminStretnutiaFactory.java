package sk.jaroslavbeno.tdd.mitingapp.domain.stretnutie;

import java.time.Duration;
import java.time.LocalTime;

public class TerminStretnutiaFactory {

    public static TerminStretnutia create(int hodina, int minuta){
        return new TerminStretnutia(LocalTime.of(hodina, minuta), Duration.ofMinutes(30));
    }
}

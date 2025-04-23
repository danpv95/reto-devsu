package e2e;

import com.intuit.karate.junit5.Karate;

public class RunKarateIT {

    @Karate.Test
    Karate testAll() {
        return Karate.run().relativeTo(getClass());
    }
}
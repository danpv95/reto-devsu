package e2e;

import com.intuit.karate.junit5.Karate;

public class RunKarateIT {

    @Karate.Test
    Karate testAll() {
        /*
         * Usa   -DbaseUrl=http://host:puerto   al invocar Maven
         * Si no se pasa, toma el valor por defecto (localhost…)
         */
        String url = System.getProperty("baseUrl", "http://localhost:8081");
        return Karate.run("classpath:e2e")     // <-- carpeta resources/e2e
                .karateEnv("qa")          // opcional para diferenciar perf/dev
                .systemProperty("baseUrl", url);
    }
}

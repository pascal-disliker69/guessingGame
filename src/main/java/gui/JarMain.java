package gui;

/**
 * Wrapper ist nötig, da die Hauptklasse nicht
 * {@link javafx.application.Application} beerben darf
 */

public class JarMain {

    public static void main(String... args) {
        ApplicationMain.main(args);
    }
}

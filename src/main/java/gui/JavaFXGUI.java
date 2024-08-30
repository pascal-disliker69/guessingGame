package gui;

import com.google.gson.JsonParseException;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import logic.GUIConnector;
import logic.highscore.HighScore;
import logic.exceptions.EmptyNameException;
import logic.exceptions.InvalidRangeException;
import logic.exceptions.NegativScoreException;
import logic.exceptions.PositionException;

import java.util.List;

/**
 * Diese Klasse nutzt JavaFX um das Interface GUIConnector zu implementieren. Sie nutzt rein logische Informationen um
 * das Spiel zu verändern und dem Nutzer darzustellen. Dabei werden abgesehen von den Namen in der Highscoreliste
 * niemals Texte aus der Logik übernommen.
 *
 * @author nvk, Annabella Hamperl
 */
public class JavaFXGUI implements GUIConnector {

    /**
     *  Die Label für die Highscore Anzeige
     *  und die Liste der Highscores darunter
     */
    private final VBox VBxName;
    private final VBox VBxPunkte;

    /**
     * VBox mit der aktuellen Zahl Anzeige und den Buttons
     */
    private final Label lblZahlenBereich;
    private final Label lblAktuelleZahl;
    private final Button btnNiedriger;
    private final Button btnHoeher;
    private final Button btnBeenden;

    /**
     * VBox mit der Namenseingabe und dem Start-/Erneutbutton
     */
    private final VBox vbxNameWahl;
    private final Button btnSpielStarten;
    private final Button btnPlayAgain;

    /**
     * VBox mit der aktuellen Punktzahl
     */
    private final VBox vbxPunktzahl;
    private final Label lblAktuellePunktzahl;

    /**
     * Label für die Infoanzeige bei zB. leerem Namen
     */
    private final Label lblInfo;
    private final Label lblName;
    private final TextField txtFldName;

    /**
     * Der Konstruktor erhält alle Komponenten die von der JavaFXGUI verändert werden sollen als Parameter und legt sie auf Klassenvariabeln ab.
     *
     * @param vbxNameWahl die VBox mit den Komponenten für die Namenseingabe
     * @param vbxPunktzahl die VBox mit den Komponenten für die Punktzahlanzeige
     *                     und den Buttons
     * @param blZahlenBereich das Label für die Anzeige des Zahlenbereichs
     * @param lblAktuelleZahl das Label für die Anzeige der aktuellen Zahl
     * @param btnHoeher der Button für die Auswahl "Höher"
     * @param btnNiedriger der Button für die Auswahl "Niedriger"
     * @param btnBeenden der Button zum Beenden des Spiels
     * @param btnSpielStarten der Button zum Starten des Spiels
     * @param btnPlayAgain der Button zum erneuten Starten des Spiels
     * @param VBxPunkte die VBox für die Anzeige der Highscores
     * @param lblAktuellePunktzahl das Label für die Anzeige der aktuellen Punktzahl
     * @param VBxName die VBox für die Anzeige der Namen in der Highscoreliste
     * @param lblInfo das Label für die Anzeige von Informationen
     * @param lblName das Label für die Anzeige des Namens
     * @param txtFldName das Textfeld für die Eingabe des Namens
     */
    public JavaFXGUI(VBox vbxNameWahl, VBox vbxPunktzahl,
                     Label blZahlenBereich, Label lblAktuelleZahl,
                     Button btnHoeher, Button btnNiedriger,
                     Button btnBeenden, Button btnSpielStarten,
                     Button btnPlayAgain, VBox VBxPunkte,
                     Label lblAktuellePunktzahl, VBox VBxName,
                     Label lblInfo, Label lblName, TextField txtFldName)
    {
        this.vbxNameWahl = vbxNameWahl;
        this.vbxPunktzahl = vbxPunktzahl;
        this.lblZahlenBereich = blZahlenBereich;
        this.lblAktuelleZahl = lblAktuelleZahl;
        this.btnHoeher = btnHoeher;
        this.btnNiedriger = btnNiedriger;
        this.btnBeenden = btnBeenden;
        this.btnSpielStarten = btnSpielStarten;
        this.btnPlayAgain = btnPlayAgain;
        this.VBxPunkte = VBxPunkte;
        this.lblAktuellePunktzahl = lblAktuellePunktzahl;
        this.VBxName = VBxName;
        this.lblInfo = lblInfo;
        this.lblName = lblName;
        this.txtFldName = txtFldName;
    }

    /**
     * Zeigt die aktuelle Nummer von der aus geraten werden soll an.
     * @param number die aktuelle Nummer.
     */
    @Override
    public void showNumber(int number) {
        lblAktuelleZahl.setText(String.valueOf(number));
    }

    /**
     * Aktualisiert die Anzeige der HighScores auf der GUI.
     * @param highScores nach Punkten absteigend sortierte Liste mit den Namen und Punkten der Spieler.
     */
    @Override
    public void updateHighScores(List<HighScore> highScores) {
        VBxName.getChildren().clear();
        VBxName.getChildren().add(new Label("Name"));

        VBxPunkte.getChildren().clear();
        VBxPunkte.getChildren().add(new Label("Punkte"));

        for (HighScore highScore : highScores)  {
            VBxName.getChildren().add(new Label(highScore.name()));
            VBxPunkte.getChildren().add(new Label(String.valueOf(
                    highScore.score())));
        }
    }

    /**
     * Aktualisiert den aktuellen Punktestand auf der GUI.
     * @param score der aktuelle Punktestand.
     */
    @Override
    public void updateCurrentScore(int score) {
        lblAktuellePunktzahl.setText(String.valueOf(score));
    }

    /**
     * Aktualisiert die Anzeige des Bereichs aus dem die Zufallszahlen stammen und in dem der Spieler raten muss.
     * @param lowerLimit untere Grenze des Ratebereichs (inklusive)
     * @param upperLimit obere Grenze des Ratebereichs (inklusive)
     */
	@Override
    public void updateRange(int lowerLimit, int upperLimit) {
        lblZahlenBereich.setText("im Bereich von " + lowerLimit + " bis "
                + upperLimit);
    }

    /**
     * Beendet das Spiel und zeigt das Ergebnis an. Die angezeigte Nachricht variiert je nachdem ob der Spieler falsch
     * geraten hat oder das Spiel selbst beendet wurde.
     * @param score das Endergebnis.
     * @param wrongGuess Gibt an ob der Spieler falsch geraten hat (true) oder das Spiel anders beendet wurde.
     * @param number die nächste Zahl.
     */
    @Override
    public void handleEndOfGame(int score, boolean wrongGuess,
                                int number)
    {
        if (wrongGuess) {

            showInformation("Spiel beendet",
                    "Leider Falsch geraten",
                    "Du hast " + score +
                    " Mal in Folge richtig geraten! Die letzte Zahl war:  "
                    + number);

            vbxNameWahl.setVisible(false);
        }
        else {
            showInformation("Spiel beendet",
                    "Du hast das Spiel beendet",
                    "Du hast " + score +
                    " Mal in Folge richtig geraten! Die letzte Zahl war:  "
                    + number);

            vbxNameWahl.setVisible(true);

            lblName.setVisible(false);

            txtFldName.setVisible(false);

            btnSpielStarten.setVisible(false);
            btnSpielStarten.setDisable(true);
        }
        vbxPunktzahl.setVisible(true);

        btnPlayAgain.setVisible(true);
        btnPlayAgain.setDisable(false);

        btnHoeher.setDisable(true);
        btnNiedriger.setDisable(true);
        btnBeenden.setDisable(true);
    }

    /**
     * Behandelt den Fall, dass die gleiche
     * Zahl nochmal gewürfelt wurde. Der Spieler erhält dann keine
     * Punkte, darf aber nochmal raten.
     * @param number die Zahl, die erneut gewürfelt wurde.
     */
    @Override
    public void handleSameNumber(int number) {
        lblInfo.setText("Die " + number +
                " wurde erneut gewürfelt. Du erhältst keinen Punkt, " +
                "darfst aber nochmal raten.");
    }

    /**
     * Setzt die Info Anzeige auf leer zurück, nachdem eine Aktion
     * getätigt wurde und die Anzeige nicht mehr wichtig ist
     */
    @Override
    public void resetInfo() {
        lblInfo.setText("");
    }

    /**
     * Zeigt eine geeignete Information für den Nutzer an, als Parameter
     * werden die einzelnen Alert Text-Bereiche übergeben
     *
     * @param title der Titel der Innformation
     * @param headerText der Header der Information
     * @param contentText der Inhalt der information
     */
    public void showInformation (String title, String headerText,
                                 String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    /**
     * Zeigt eine geeignete Fehlermeldung für den Nutzer an,
     * basierend auf der Typ der übergebenen Exception. Dabei wird
     * NICHT der Detailtext der Exception verwendet.
     *
     * @param e der Fehler, der aufgetreten ist.
     */
    @Override
    public void showException(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler beim Laden der Highscore-Daten");
        alert.setHeaderText("Ein Fehler ist aufgetreten.");

            if (e instanceof InvalidRangeException) {
                alert.setContentText("Die JSON-Datei hat eine ungültige " +
                        "Struktur: Ungültiger Zahlenbereich" + e.getMessage());
            }

            if (e instanceof JsonParseException) {
                alert.setContentText("Die JSON-Datei hat eine ungültige " +
                        "Struktur");
            }
            alert.showAndWait();
    }

    /**
     * Zeigt eine geeignete Fehlermeldung für den Nutzer an,
     * basierend auf der Typ der übergebenen Exception. Dabei wird
     * NICHT der Detailtext der Exception verwendet.
     *
     * @param e der Fehler, der aufgetreten ist.
     */
    public void showPositionException(PositionException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler beim Laden der Highscore-Daten");
        alert.setHeaderText("Ein Fehler ist aufgetreten.");

            if (e instanceof EmptyNameException) {
                alert.setContentText("Ein Name in der Highscore-Liste ist " +
                        "leer. Position: " + e.getPosition());
            }
            else if (e instanceof NegativScoreException) {
                NegativScoreException negativScoreException =
                        (NegativScoreException) e;
                alert.setContentText("Ein Score in der Highscore-Liste ist " +
                        "negativ. Position: " + e.getPosition() + " Score: " +
                        negativScoreException.getNegativScore());
            }
            alert.showAndWait();
    }
}

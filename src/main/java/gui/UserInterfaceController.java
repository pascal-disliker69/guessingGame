package gui;


import com.google.gson.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.Game;
import logic.highscore.HighScore;
import logic.highscore.HighScoreFileHandler;
import logic.exceptions.EmptyNameException;
import logic.exceptions.InvalidRangeException;
import logic.exceptions.NegativScoreException;

import java.io.*;
import java.net.URISyntaxException;

/**
 * Der Controller der GUI. Er erstellt bei Programmstart das game und die JavaFXGUI. Im Spielverlauf verarbeitet er die
 * Eingaben des Nutzers und gibt diese an das game weiter.
 *
 * @author nvk, Annabella Hamperl
 */
public class UserInterfaceController {

    /**
     * Die Logik des Spiels.
     */
    private Game game;
    /**
     * Die Implementation des GUIConnectors, die die Logik nutzt um mit der GUI zu kommunizieren.
     */
    private JavaFXGUI gui;
    @FXML
    private AnchorPane anchrPnMain;

    /**
     * Die Label für die Highscore Anzeige
     * und die Liste der Highscores darunter
     */
    @FXML
    private VBox VBxPunkte;
    @FXML
    private VBox VBxName;

    /**
     * VBox mit der aktuellen Zahl Anzeige und den Buttons
     */
    @FXML
    private Label lblZahlenBereich;
    @FXML
    private Label lblAktuelleZahl;
    @FXML
    private Button btnNiedriger;
    @FXML
    private Button btnHoeher;
    @FXML
    private Button btnBeenden;

    /**
     * VBox mit der Namenseingabe und dem Start-/Erneutbutton
     */
    @FXML
    private VBox vbxNameWahl;
    @FXML
    private Button btnSpielStarten;
    @FXML
    private Button btnPlayAgain;

    /**
     * VBox mit der aktuellen Punktzahl
     */
    @FXML
    private VBox vbxPunktzahl;
    @FXML
    private Label lblAktuellePunktzahl;

    /**
     * Label für die Infoanzeige bei zB. leerem Namen
     */
    @FXML
    private Label lblInfo;
    @FXML
    private Label lblName;
    @FXML
    private TextField txtFldName;

    /**
     * Initialisiert die GUI und das Spiel für den ersten Start.
     */
    @FXML
    public void initialize() {
        gui = new JavaFXGUI(vbxNameWahl, vbxPunktzahl,
                lblZahlenBereich, lblAktuelleZahl, btnHoeher, btnNiedriger,
                btnBeenden,btnSpielStarten, btnPlayAgain,  VBxPunkte,
                lblAktuellePunktzahl, VBxName, lblInfo, lblName, txtFldName);
        game = new Game(gui);
        vbxPunktzahl.setVisible(true);
        btnPlayAgain.setVisible(false);
        lblAktuellePunktzahl.setText("");
        vbxNameWahl.setVisible(false);

        gui.updateRange(1, 10);
        gui.updateHighScores(game.getHighScores());
        lblAktuelleZahl.setText("");
        btnNiedriger.setDisable(true);
        btnHoeher.setDisable(true);
        btnBeenden.setDisable(true);
    }

    /**
     * Behandelt den Fall, dass der Nutzer auf den "Neues Spiel"
     * Button klickt.
     * @param actionEvent
     */
    @FXML
    private void handleOnNewGameButtonClick(ActionEvent actionEvent) {
        vbxPunktzahl.setVisible(false);
        vbxNameWahl.setVisible(true);
        lblName.setVisible(true);
        txtFldName.setVisible(true);

        btnSpielStarten.setVisible(true);
        btnPlayAgain.setVisible(false);
        btnSpielStarten.setDisable(false);

        lblAktuelleZahl.setText("");
        btnNiedriger.setDisable(true);
        btnHoeher.setDisable(true);
        btnBeenden.setDisable(true);
    }

    /**
     * Fehlerbehandlung beim Laden einer Highscoredatei
     * @return HighScoreData Die geladenen Highscore-Daten aus der
     * Datei
     * @throws EmptyNameException im Fall, dass ein Name in der Datei
     * leer war
     * @throws NegativScoreException im Fall, dass ein Highscore aus
     * der Datei negativ war
     */
    public HighScoreFileHandler loadHighScoreData() throws
            EmptyNameException, NegativScoreException
    {
        FileChooser fileChooser = getFileChooser();
        File file = fileChooser.showOpenDialog(
                anchrPnMain.getScene().getWindow());

        if (file != null) {
            try (Reader reader0 = new FileReader(file)) {

                if (file.length() == 0) {
                    throw new JsonParseException("Die Datei ist leer.");
                }

                // Inhalt der Datei überprüfen
                JsonElement jsonElement = JsonParser.parseReader(reader0);
                if (jsonElement == null || !jsonElement.isJsonObject()) {
                    throw new JsonParseException("Die JSON-Datei hat eine" +
                            " ungültige Struktur.");
                }

                Reader reader1 = new FileReader(file);

                checkParseException(reader1);
                reader1.close();

                Reader reader2 = new FileReader(file);
                Gson gson = new Gson();
                HighScoreFileHandler highScoreFileHandler = gson.fromJson(reader2,
                        HighScoreFileHandler.class);
                reader2.close();

                checkEmptyNameException(highScoreFileHandler);

                checkNegativeScoreException(highScoreFileHandler);

                try (Reader reader3 = new FileReader(file)) {
                    checkInvalidRangeException(reader3);
                }

                return highScoreFileHandler;
            }

            catch (EmptyNameException | NegativScoreException  e) {
                // Handling der speziellen Ausnahme
                gui.showPositionException(e);
                throw e;
            }
            catch (IOException e) {
                // Fehlerbehandlung beim Laden
                System.err.println("Fehler beim Laden der Highscore-Daten: "
                        + e.getMessage());
                gui.showException(e);
                e.printStackTrace();
            }
            catch (JsonParseException | InvalidRangeException e) {
                // Fehlerbehandlung beim Parsen
                System.err.println("Fehler beim Parsen der JSON-Datei: " +
                        e.getMessage());
                gui.showException(e);
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Erstellt einen FileChooser für das Öffnen von Dateien
     * @return FileChooser Der FileChooser für das Öffnen von Dateien
     */
    private static FileChooser getFileChooser() {
        File currDir = null;
        try {
            currDir = new File(UserInterfaceController.class.
                    getProtectionDomain().getCodeSource().getLocation().
                    toURI());
        } catch (URISyntaxException ex) {
            System.err.println("Fehler beim Ermitteln des aktuellen " +
                    "Verzeichnisses: " + ex.getMessage());
            ex.printStackTrace();
        }
//Step 2: Put it together
        FileChooser fileChooser = new FileChooser();
        if (currDir != null) {
            //ensure the dialog opens in the correct directory
            fileChooser.setInitialDirectory(currDir.getParentFile());
        }
        fileChooser.setTitle("Open JSON Graph-File");
        return fileChooser;
    }

    /**
     * Hilfsmethode die auf eine JsonParseException prüft
     * wirft eine Exception, wenn:
     * - Die Datei ein fehlendes Feld (highScores, lowerLimit, upperLimit)
     * hat
     * - highScores kein Array ist
     * - Ein HighScore-Objekt im Array kein Objekt ist
     * - Ein HighScore-Objekt im Array kein `name` oder `score` Feld
     * hat
     *
     * @param reader Der Reader, der die Datei einliest
     */
    public void checkParseException(Reader reader) {
        JsonObject jsonObject = JsonParser.parseReader(reader).
                getAsJsonObject();

        // Überprüfe, ob die erwarteten Felder vorhanden sind
        if (!jsonObject.has("highScores") ||
                !jsonObject.has("lowerLimit") ||
                !jsonObject.has("upperLimit")) {
            System.out.println("Die JSON-Datei hat eine ungültige Struktur: "
                    + "Fehlende Felder");
            throw new JsonParseException("Die JSON-Datei hat eine" +
                    " ungültige Struktur: Fehlende Felder");
        }
        if (!jsonObject.get("highScores").isJsonArray()) {
            System.out.println("`highScores` sollte ein Array sein.");
            throw new JsonParseException("`highScores` sollte " +
                    "ein Array sein.");
        }
        JsonArray highScoresArray = jsonObject.getAsJsonArray(
                "highScores");
        for (JsonElement element : highScoresArray) {
            if (!element.isJsonObject()) {
                System.out.println("Ungültiges HighScore-Objekt im Array.");
                throw new JsonParseException("Ungültiges " +
                        "HighScore-Objekt im Array.");
            }
            JsonObject highScoreObj = element.getAsJsonObject();

            if (!highScoreObj.has("name") ||
                    !highScoreObj.has("score")) {
                System.out.println("HighScore-Objekt fehlt `name` oder " +
                        "`score` Feld.");
                throw new JsonParseException("HighScore-Objekt fehlt " +
                        "`name` oder `score` Feld.");
            }
        }
    }

    /**
     * Hilfsmethode die auf eine PositionException prüft
     * @param highScoreFileHandler Die Highscore-Daten, die überprüft werden
     * @throws EmptyNameException im Fall, dass ein Name in der Datei
     * leer war
     */
    public void checkEmptyNameException(HighScoreFileHandler
                                                highScoreFileHandler)
            throws EmptyNameException {
        for (int i = 0; i < highScoreFileHandler.getHighScores().size(); i++) {
            HighScore highScore = highScoreFileHandler.getHighScores().
                    getHighScoreAt(i);
            if (highScore.name() == null || highScore.name().trim().
                    isEmpty()) {
                System.out.println("Ein Name in der Highscore-Liste ist leer.");
                throw new EmptyNameException("Ein Name in der " +
                        "Highscore-Liste ist leer.", i);
            }
        }
    }

    /**
     * Hilfsmethode die auf eine NegativScoreException prüft
     * @param highScoreFileHandler Die Highscore-Daten, die überprüft
     *                      werden
     * @throws NegativScoreException im Fall, dass ein Score in der
     * Highscore-Liste negativ war
     */
    public void checkNegativeScoreException(HighScoreFileHandler
                                                    highScoreFileHandler)
            throws NegativScoreException {
        for (int i = 0; i < highScoreFileHandler.getHighScores().size(); i++) {
            HighScore highScore = highScoreFileHandler.getHighScores().
                    getHighScoreAt(i);
            if (highScore.score() < 0) {
                System.out.println("Ein Score in der Highscore-Liste ist " +
                        "negativ.");
                throw new NegativScoreException("Ein Score in der " +
                        "Highscore-Liste ist negativ.", i, highScore.score());
            }
        }
    }

    /**
     * Hilfsmethode die auf eine InvalidRangeException prüft
     * @param reader Der Reader, der die Datei einliest
     * @throws InvalidRangeException im Fall, dass der Zahlenbereich
     * ungültig ist (lowerLimit >= upperLimit oder negativ)
     */
    public void checkInvalidRangeException(Reader reader)
            throws InvalidRangeException {
        JsonObject jsonObject = JsonParser.parseReader(reader).
                getAsJsonObject();
        int lowerLimit = jsonObject.get("lowerLimit").getAsInt();
        int upperLimit = jsonObject.get("upperLimit").getAsInt();

        if ((lowerLimit>=upperLimit) || (lowerLimit<=0) ||
                (upperLimit<=0)) {
            System.out.println("Die JSON-Datei hat eine ungültige Struktur:"
                    + " Ungültiger Zahlenbereich");
            throw new InvalidRangeException("Die JSON-Datei hat eine " +
                    "ungültige Struktur: Ungültiger Zahlenbereich", lowerLimit,
                    upperLimit);
        }
    }

    /**
     * Behandelt den Fall, dass der Nutzer auf den "Highscore laden"
     * Button klickt.
     * @param actionEvent
     * @throws NegativScoreException im Fall, dass ein Score in der
     * Highscore-Liste negativ war
     * @throws EmptyNameException im Fall, dass ein Name in der Datei
     * leer war
     */
    @FXML
    private void handleOnLoadHighscoreClick(ActionEvent actionEvent)
            throws NegativScoreException, EmptyNameException {
        HighScoreFileHandler highScoreFileHandler = loadHighScoreData();

        if (highScoreFileHandler != null) {
            gui.updateHighScores(highScoreFileHandler.highScores);
            game.setHighScores(highScoreFileHandler.getHighScores());

            game.setLimits(highScoreFileHandler.getLowerLimit(),
                    highScoreFileHandler.getUpperLimit());
            gui.updateRange(highScoreFileHandler.getLowerLimit(),
                    highScoreFileHandler.getUpperLimit());
        }
    }

    /**
     * Behandelt den Fall, dass der Nutzer auf den "Highscore speichern"
     * Button klickt.
     */
    @FXML
    private void handleOnSaveHighscoreButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Highscore");
        fileChooser.getExtensionFilters().add(new FileChooser.
                ExtensionFilter("JSON", "*.json"));

        File file = fileChooser.showSaveDialog(anchrPnMain.
                getScene().getWindow());
        try {
            if (file != null) {
                //Fügt ".json" Erweiterung hinzu, falls nicht vorhanden
                if (!file.getName().endsWith(".json")) {
                    file = new File(file.getAbsolutePath() + ".json");
                }

                // Speichern der Highscore-Daten
                game.saveScore(file);
            } else {
                System.out.println("File selection was cancelled.");
            }
        } catch (IOException e) {
            // Fehlerbehandlung beim Speichern
            System.err.println("Failed to save highscore data: " +
                    e.getMessage());
            e.printStackTrace();
        }
        }

    /**
     * Behandelt den Fall, dass der Nutzer auf den "Beenden"
     * Button klickt.
     * @param actionEvent
     */
    @FXML
    private void handleOnCloseButtonClick(ActionEvent actionEvent) {
        Stage stage = (Stage) anchrPnMain.getScene().getWindow();
        stage.close();
    }

    /**
     * Behandelt den Fall, dass der Nutzer auf den "Spiel starten"
     * Button klickt.
     */
    @FXML
    private void handleOnStartGameButtonClick() {
        String name = txtFldName.getText().trim();
        if (!name.isEmpty()) {
            gui.resetInfo();
            vbxNameWahl.setVisible(false);
            vbxPunktzahl.setVisible(true);

            btnNiedriger.setDisable(false);
            btnHoeher.setDisable(false);
            btnBeenden.setDisable(false);

            game.startNewGame(name);
            lblAktuelleZahl.setVisible(true);
        }
        else {
            lblInfo.setText("Name darf nicht leer sein. " +
                    "Bitte gib einen Namen ein");
        }
    }

    /**
     * Behandelt den Fall, dass der Nutzer auf den "Niedriger"
     * Button klickt.
     * @param actionEvent
     */
    @FXML
    private void handleOnLowerButtonClick(ActionEvent actionEvent) {
        game.guessLower();
    }

    /**
     * Behandelt den Fall, dass der Nutzer auf den "Höher"
     * Button klickt.
     * @param actionEvent
     */
    @FXML
    private void handleOnHigherButtonClick(ActionEvent actionEvent) {
        game.guessHigher();
    }

    /**
     * Behandelt den Fall, dass der Nutzer auf den "Beenden"
     * Button klickt.
     * @param actionEvent
     */
    @FXML
    private void handleOnEndButtonClick(ActionEvent actionEvent) {
        game.endGame();
    }

    /**
     * Behandelt den Fall, dass der Nutzer auf den "Erneut spielen"
     * Button klickt.
     * @param actionEvent
     */
    @FXML
    private void handleOnPlayAgainButton(ActionEvent actionEvent) {
        gui.resetInfo();
        vbxNameWahl.setVisible(false);
        vbxPunktzahl.setVisible(true);

        btnPlayAgain.setVisible(false);
        btnNiedriger.setDisable(false);
        btnHoeher.setDisable(false);
        btnBeenden.setDisable(false);

        game.startNewGame(game.getCurrentPlayer());
        lblAktuelleZahl.setVisible(true);
    }
}

package logic;

import logic.highscore.HighScore;

import java.util.List;

/**
 * Implementiert das Interface GUIConnector, damit es in Tests verwendet werden kann.
 *
 * @author nvk
 */
public class FakeGUI implements GUIConnector{
    @Override
    public void showNumber(int number) {

    }

    @Override
    public void updateHighScores(List<HighScore> highScores) {

    }

    @Override
    public void updateCurrentScore(int score) {

    }

    @Override
    public void updateRange(int lowerLimit, int upperLimit) {

    }

    @Override
    public void handleEndOfGame(int score, boolean wrongGuess, int number) {

    }

    @Override
    public void handleSameNumber(int number) {

    }

    @Override
    public void showException(Exception e) {

    }
}

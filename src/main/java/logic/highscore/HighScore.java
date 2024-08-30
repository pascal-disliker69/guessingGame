package logic.highscore;

/**
 * Der HighScore eines Spielers mit dessen Namen und der erreichten Punktzahl.
 *
 * @param name  Der Name des Spielers.
 * @param score Die erreichte Punktzahl.
 * @author nvk
 */
public record HighScore(String name, int score) {}
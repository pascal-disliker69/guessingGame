package logic;

import logic.highscore.HighScore;
import logic.highscore.HighScoreList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PubHighScoreListTest {

    @Test
    public void testAdd_EmptyList_OneElement() {
        HighScoreList result = new HighScoreList();
        HighScore insert = new HighScore("A", 1);
        result.add(insert);
        assertEquals(1, result.getHighScores().size());
        assertEquals(insert, result.getHighScores().get(0));
    }

    @Test
    public void testAdd_EmptyList_ThreeElementsRightOrder() {
        HighScoreList result = new HighScoreList();
        HighScore a = new HighScore("A", 3);
        HighScore b = new HighScore("B", 2);
        HighScore c = new HighScore("C", 1);
        result.add(a);
        result.add(b);
        result.add(c);
        assertEquals(3, result.getHighScores().size());
        assertEquals(a, result.getHighScores().get(0));
        assertEquals(b, result.getHighScores().get(1));
        assertEquals(c, result.getHighScores().get(2));
    }

    @Test
    public void testAdd_EmptyList_ThreeElementsWrongOrder() {
        HighScoreList result = new HighScoreList();
        HighScore a = new HighScore("A", 3);
        HighScore b = new HighScore("B", 2);
        HighScore c = new HighScore("C", 1);
        result.add(b);
        result.add(a);
        result.add(c);
        assertEquals(3, result.getHighScores().size());
        assertEquals(a, result.getHighScores().get(0));
        assertEquals(b, result.getHighScores().get(1));
        assertEquals(c, result.getHighScores().get(2));
    }

    @Test
    public void testAdd_EmptyList_ThreeElementsHighestScoreFirst() {
        HighScoreList result = new HighScoreList();
        HighScore a = new HighScore("A", 10);
        HighScore b = new HighScore("B", 1);
        HighScore c = new HighScore("C", 5);
        result.add(a);
        result.add(c);
        result.add(b);
        assertEquals(3, result.getHighScores().size());
        assertEquals(a, result.getHighScores().get(0));
        assertEquals(c, result.getHighScores().get(1));
        assertEquals(b, result.getHighScores().get(2));
    }

    @Test
    public void testAdd_EmptyList_ThreeElementsLowestScoreFirst() {
        HighScoreList result = new HighScoreList();
        HighScore a = new HighScore("A", 10);
        HighScore b = new HighScore("B", 1);
        HighScore c = new HighScore("C", 5);
        result.add(b);
        result.add(c);
        result.add(a);
        assertEquals(3, result.getHighScores().size());
        assertEquals(a, result.getHighScores().get(0));
        assertEquals(c, result.getHighScores().get(1));
        assertEquals(b, result.getHighScores().get(2));
    }

    @Test
    public void testAdd_EmptyList_ThreeElementsMixedScores() {
        HighScoreList result = new HighScoreList();
        HighScore a = new HighScore("A", 10);
        HighScore b = new HighScore("B", 1);
        HighScore c = new HighScore("C", 5);
        result.add(b);
        result.add(a);
        result.add(c);
        assertEquals(3, result.getHighScores().size());
        assertEquals(a, result.getHighScores().get(0));
        assertEquals(c, result.getHighScores().get(1));
        assertEquals(b, result.getHighScores().get(2));
    }
}

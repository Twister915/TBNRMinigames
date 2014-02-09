package net.tbnr.sg;

import junit.framework.TestCase;
import net.tbnr.minigame.sg.GSurvivalGamesGame;

/**
 * Created by Joey on 2/6/14.
 *
 * Purpose Of File: TO serve as the survival games test
 *
 * Latest Change: Added file template correction
 */
public class GSurvivalGamesGameTest extends TestCase {

    public GSurvivalGamesGameTest() {
        super("GSurvivalGamesGame");
    }

    public void testContains() {
        Integer[] integers = new Integer[]{30, 15, 10, 5, 0};
        assertTrue(GSurvivalGamesGame.contains(integers, 30));
        assertTrue(GSurvivalGamesGame.contains(integers, 15));
        assertTrue(GSurvivalGamesGame.contains(integers, 10));
        assertTrue(GSurvivalGamesGame.contains(integers, 5));
        assertTrue(GSurvivalGamesGame.contains(integers, 0));
        assertTrue(!GSurvivalGamesGame.contains(integers, 9));
    }
}

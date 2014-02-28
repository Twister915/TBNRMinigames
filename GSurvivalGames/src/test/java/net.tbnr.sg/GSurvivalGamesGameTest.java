/*
 * Copyright (c) 2014.
 * Cogz Development LLC USA
 * All Right reserved
 *
 * This software is the confidential and proprietary information of Cogz Development, LLC.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with Cogz LLC.
 */

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

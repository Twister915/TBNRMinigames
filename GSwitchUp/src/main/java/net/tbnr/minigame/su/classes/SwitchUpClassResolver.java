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

package net.tbnr.minigame.su.classes;

import net.tbnr.gearz.Gearz;
import net.tbnr.gearz.game.GearzGame;
import net.tbnr.manager.TBNRPlayer;
import net.tbnr.manager.classes.TBNRAbstractClass;
import net.tbnr.manager.classes.TBNRClassResolver;
import net.tbnr.minigame.su.GSwitchUpGame;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class SwitchUpClassResolver extends TBNRClassResolver {

    private Map<TBNRPlayer, Class<? extends TBNRAbstractClass>> classes;

    public void shuffleClasses(GSwitchUpGame game) {
        this.classes = new HashMap<>();
        for (TBNRPlayer tbnrPlayer : game.getPlayers()) {
            shufflePlayer(tbnrPlayer);
        }
    }

    public void shufflePlayer(TBNRPlayer player) {
        Class<? extends TBNRAbstractClass>[] classes = getClassSystem().getClasses();
        this.classes.put(player, classes[Gearz.getRandom().nextInt(classes.length)]);
    }

    @Override
    public Class<? extends TBNRAbstractClass> getClassForPlayer(TBNRPlayer player, GearzGame<TBNRPlayer, TBNRAbstractClass> game) {
        return this.classes.get(player);
    }

    @Override
    public void playerUsedClassFully(TBNRPlayer player, TBNRAbstractClass classUsed, GearzGame<TBNRPlayer, TBNRAbstractClass> game) {

    }

    @Override
    public void gameStarting(Collection<TBNRPlayer> players, GearzGame<TBNRPlayer, TBNRAbstractClass> game) {
        shuffleClasses((GSwitchUpGame) game);
    }

    @Override
    public boolean canUseClass(TBNRPlayer player, Class<? extends TBNRAbstractClass> clazz) {
        return false;
    }
}

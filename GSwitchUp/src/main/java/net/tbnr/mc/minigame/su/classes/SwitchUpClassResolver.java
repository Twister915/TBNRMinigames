/*
 * Copyright (c) 2014.
 * CogzMC LLC USA
 * All Right reserved
 *
 * This software is the confidential and proprietary information of Cogz Development, LLC.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with Cogz LLC.
 */

package net.tbnr.mc.minigame.su.classes;

import net.cogzmc.engine.gearz.Gearz;
import net.cogzmc.engine.gearz.game.GearzGame;
import net.tbnr.mc.manager.TBNRPlayer;
import net.tbnr.mc.manager.classes.TBNRAbstractClass;
import net.tbnr.mc.manager.classes.TBNRClassResolver;
import net.tbnr.mc.minigame.su.GSwitchUpGame;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SwitchUpClassResolver extends TBNRClassResolver {

    private Map<TBNRPlayer, Class<? extends TBNRAbstractClass>> classes;

    public SwitchUpClassResolver() {
        super(null, null);
    }

    public void shuffleClasses(GSwitchUpGame game) {
        this.classes = new HashMap<>();
        for (TBNRPlayer tbnrPlayer : game.getPlayers()) {
            shufflePlayer(tbnrPlayer);
        }
    }

    public void shufflePlayer(TBNRPlayer player) {
        List<Class<? extends TBNRAbstractClass>> classes = getClassSystem().getClasses();
        assignPlayerClass(player, classes.get(Gearz.getRandom().nextInt(classes.size())));
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

	public void assignPlayerClass(TBNRPlayer player, Class<? extends TBNRAbstractClass> clazz) {
		classes.put(player, clazz);
	}
}

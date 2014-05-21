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

package net.tbnr.mc.manager.classes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cogzmc.engine.gearz.game.GearzGame;
import net.cogzmc.engine.gearz.game.classes.GearzClassResolver;
import net.tbnr.mc.manager.TBNRPlayer;
import net.tbnr.mc.manager.classes.pass.ClassPassManager;

import java.util.Collection;

@EqualsAndHashCode(callSuper = false)
@Data
public class TBNRClassResolver extends GearzClassResolver<TBNRPlayer,TBNRAbstractClass> {
    private final ClassPassManager<TBNRAbstractClass> classPassManager;
    private final Class<? extends TBNRAbstractClass> defaultClass;

    @Override
    public Class<? extends TBNRAbstractClass> getClassForPlayer(TBNRPlayer player, GearzGame<TBNRPlayer, TBNRAbstractClass> game) {
        if (!game.getClass().isAnnotationPresent(UsesClasses.class)) return defaultClass;
        Class<? extends TBNRAbstractClass> classForGame = classPassManager.getClassForGame(player);
        return classForGame == null ? defaultClass : classForGame;
    }

    @Override
    public void playerUsedClassFully(TBNRPlayer player, TBNRAbstractClass classUsed, GearzGame<TBNRPlayer, TBNRAbstractClass> game) {
        classPassManager.playerHasUsedClass(classUsed.getClass(), player);
    }

    @Override
    public void gameStarting(Collection<TBNRPlayer> players, GearzGame<TBNRPlayer, TBNRAbstractClass> game) {
        for (TBNRPlayer player : players) {
            classPassManager.setLastUsedClass(player, game.getClassFor(player).getClass());
        }
    }
}

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

package net.tbnr.gearz.hh.classes;

import net.tbnr.gearz.game.GearzGame;
import net.tbnr.gearz.hh.classes.def.Normal;
import net.tbnr.manager.TBNRPlayer;
import net.tbnr.manager.classes.TBNRAbstractClass;
import net.tbnr.manager.classes.TBNRClassResolver;

import java.util.*;
import java.util.logging.Logger;

/**
 * <p/>
 * Latest Change:
 * <p/>
 *
 * @author George
 * @since 13/04/14
 */
public class HeadHunterClassResolver extends TBNRClassResolver {
	private static final boolean debug = false; //TODO get debug mode
	private static final Logger log = null; //TODO get logger

	private Map<TBNRPlayer, Class<? extends TBNRAbstractClass>> classes;

	public void assignClass(TBNRPlayer player, Class<? extends TBNRAbstractClass> clazz) {
		List<Class<? extends TBNRAbstractClass>> availableClasses = new ArrayList<>();
		Collections.addAll(availableClasses, getClassSystem().getClasses());
		if(!availableClasses.contains(clazz)) return;
		classes.put(player, clazz);
	}

	@Override
	public Class<? extends TBNRAbstractClass> getClassForPlayer(TBNRPlayer player, GearzGame<TBNRPlayer, TBNRAbstractClass> game) {
		return classes.get(player);
	}

	@Override
	public void playerUsedClassFully(TBNRPlayer player, TBNRAbstractClass classUsed, GearzGame<TBNRPlayer, TBNRAbstractClass> game) {
		// ???
	}

	@Override
	public void gameStarting(Collection<TBNRPlayer> players, GearzGame<TBNRPlayer, TBNRAbstractClass> game) {
		classes = new HashMap<>();
		for(TBNRPlayer player : players) {
			classes.put(player, Normal.class);
		}
	}

	@Override
	public boolean canUseClass(TBNRPlayer player, Class<? extends TBNRAbstractClass> clazz) {
		return false;
	}

	public void assignPlayerClass(TBNRPlayer player, Class<? extends TBNRAbstractClass> clazz) {
		classes.put(player, clazz);
	}
}

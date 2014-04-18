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

package net.tbnr.gearz.cm;

import net.tbnr.gearz.Gearz;
import net.tbnr.gearz.GearzPlugin;
import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.game.GameMeta;
import net.tbnr.gearz.network.GearzPlayerProvider;
import net.tbnr.manager.TBNRMinigame;
import net.tbnr.manager.TBNRPlayer;
import net.tbnr.manager.classes.TBNRAbstractClass;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.logging.Logger;

/**
 * <p/>
 * Latest Change:
 * <p/>
 *
 * @author George
 * @since 04/04/14
 */
public class CatAndMouseGame extends TBNRMinigame {
	private static final boolean debug = Gearz.getInstance().showDebug();
	private static final Logger log = Gearz.getInstance().getLogger();

	CatAndMouseArena catAndMouseArena;

	/**
	 * New game in this arena
	 *
	 * @param players        The players in this game
	 * @param arena          The Arena that the game is in.
	 * @param plugin         The plugin that handles this Game.
	 * @param meta           The meta of the game.
	 * @param id
	 * @param playerProvider
	 */
	public CatAndMouseGame(List<TBNRPlayer> players, Arena arena, GearzPlugin<TBNRPlayer, TBNRAbstractClass> plugin, GameMeta meta, Integer id, GearzPlayerProvider<TBNRPlayer> playerProvider) {
		super(players, arena, plugin, meta, id, playerProvider);
		if (!(arena instanceof CatAndMouseArena)) throw new RuntimeException("Invalid game class");
		this.catAndMouseArena = (CatAndMouseArena) arena;
	}

	@Override
	protected int xpForPlaying() {
		if (debug) {
			log.info("xpForPlaying() was called in class net.tbnr.gearz.cm.CatAndMouseGame! It Normally Returns int!");
		}
		return 100;
	}
	@Override
	protected void gameStarting() {
		if (debug) {
			log.info("gameStarting() was called in class net.tbnr.gearz.cm.CatAndMouseGame! It Normally Returns void!");
		}
		// import org.apache.commons.lang.NotImplementedException;
		throw new NotImplementedException("gameStarting() has not been created yet in class net.tbnr.gearz.cm.CatAndMouseGame! It would Normally Return void!");
	}

	@Override
	protected void gameEnding() {
		if (debug) {
			log.info("gameEnding() was called in class net.tbnr.gearz.cm.CatAndMouseGame! It Normally Returns void!");
		}
		// import org.apache.commons.lang.NotImplementedException;
		throw new NotImplementedException("gameEnding() has not been created yet in class net.tbnr.gearz.cm.CatAndMouseGame! It would Normally Return void!");
	}

	@Override
	protected boolean canBuild(TBNRPlayer player) {
		if (debug) {
			log.info("canBuild() was called in class net.tbnr.gearz.cm.CatAndMouseGame! It Normally Returns boolean!");
		}
		// import org.apache.commons.lang.NotImplementedException;
		throw new NotImplementedException("canBuild() has not been created yet in class net.tbnr.gearz.cm.CatAndMouseGame! It would Normally Return boolean!");
	}

	@Override
	protected boolean canPvP(TBNRPlayer attacker, TBNRPlayer target) {
		if (debug) {
			log.info("canPvP() was called in class net.tbnr.gearz.cm.CatAndMouseGame! It Normally Returns boolean!");
		}
		return true;
	}

	@Override
	protected boolean canUse(TBNRPlayer player) {
		if (debug) {
			log.info("canUse() was called in class net.tbnr.gearz.cm.CatAndMouseGame! It Normally Returns boolean!");
		}
		return true;
	}

	@Override
	protected boolean canBreak(TBNRPlayer player, Block block) {
		if (debug) {
			log.info("canBreak() was called in class net.tbnr.gearz.cm.CatAndMouseGame! It Normally Returns boolean!");
		}
		return false;
	}

	@Override
	protected boolean canPlace(TBNRPlayer player, Block block) {
		if (debug) {
			log.info("canPlace() was called in class net.tbnr.gearz.cm.CatAndMouseGame! It Normally Returns boolean!");
		}
		return false;
	}

	@Override
	protected boolean canMove(TBNRPlayer player) {
		if (debug) {
			log.info("canMove() was called in class net.tbnr.gearz.cm.CatAndMouseGame! It Normally Returns boolean!");
		}
		return true;
	}

	@Override
	protected boolean canDrawBow(TBNRPlayer player) {
		if (debug) {
			log.info("canDrawBow() was called in class net.tbnr.gearz.cm.CatAndMouseGame! It Normally Returns boolean!");
		}
		return true;
	}

	@Override
	protected void playerKilled(TBNRPlayer dead, LivingEntity killer) {
		if (debug) {
			log.info("playerKilled() was called in class net.tbnr.gearz.cm.CatAndMouseGame! It Normally Returns void!");
		}
		// import org.apache.commons.lang.NotImplementedException;
		throw new NotImplementedException("playerKilled() has not been created yet in class net.tbnr.gearz.cm.CatAndMouseGame! It would Normally Return void!");
	}

	@Override
	protected void playerKilled(TBNRPlayer dead, TBNRPlayer killer) {
		if (debug) {
			log.info("playerKilled() was called in class net.tbnr.gearz.cm.CatAndMouseGame! It Normally Returns void!");
		}
		// import org.apache.commons.lang.NotImplementedException;
		throw new NotImplementedException("playerKilled() has not been created yet in class net.tbnr.gearz.cm.CatAndMouseGame! It would Normally Return void!");
	}

	@Override
	protected void mobKilled(LivingEntity killed, TBNRPlayer killer) {
		if (debug) {
			log.info("mobKilled() was called in class net.tbnr.gearz.cm.CatAndMouseGame! It Normally Returns void!");
		}
	}

	@Override
	protected boolean canDropItem(TBNRPlayer player, ItemStack itemToDrop) {
		if (debug) {
			log.info("canDropItem() was called in class net.tbnr.gearz.cm.CatAndMouseGame! It Normally Returns boolean!");
		}
		return false;
	}

	@Override
	protected Location playerRespawn(TBNRPlayer player) {
		if (debug) {
			log.info("playerRespawn() was called in class net.tbnr.gearz.cm.CatAndMouseGame! It Normally Returns Location!");
		}
		return getArena().pointToLocation(this.catAndMouseArena.SpawnPoints.next());
	}

	@Override
	protected boolean canPlayerRespawn(TBNRPlayer player) {
		if (debug) {
			log.info("canPlayerRespawn() was called in class net.tbnr.gearz.cm.CatAndMouseGame! It Normally Returns boolean!");
		}
		return true;
	}

	@Override
	protected void activatePlayer(TBNRPlayer player) {
		if (debug) {
			log.info("activatePlayer() was called in class net.tbnr.gearz.cm.CatAndMouseGame! It Normally Returns void!");
		}
		// import org.apache.commons.lang.NotImplementedException;
		throw new NotImplementedException("activatePlayer() has not been created yet in class net.tbnr.gearz.cm.CatAndMouseGame! It would Normally Return void!");
	}

	@Override
	protected boolean allowHunger(TBNRPlayer player) {
		if (debug) {
			log.info("allowHunger() was called in class net.tbnr.gearz.cm.CatAndMouseGame! It Normally Returns boolean!");
		}
		return false;
	}
}

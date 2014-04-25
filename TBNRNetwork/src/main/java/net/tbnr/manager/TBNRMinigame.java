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

package net.tbnr.manager;

import net.tbnr.gearz.GearzPlugin;
import net.tbnr.gearz.arena.Arena;
import net.tbnr.gearz.game.GameMeta;
import net.tbnr.gearz.game.GameStopCause;
import net.tbnr.gearz.game.GearzGame;
import net.tbnr.gearz.network.GearzPlayerProvider;
import net.tbnr.manager.classes.TBNRAbstractClass;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

public abstract class TBNRMinigame extends GearzGame<TBNRPlayer, TBNRAbstractClass> {
    private HashMap<TBNRPlayer, Integer> pendingPoints;

    /**
     * New game in this arena
     *
     * @param players        The players in this game
     * @param arena          The Arena that the game is in.
     * @param plugin         The plugin that handles this Game.
     * @param meta           The meta of the game.
     * @param id             Game ID
     * @param playerProvider {@link net.tbnr.manager.TBNRNetworkManager} {@link net.tbnr.gearz.network.GearzPlayerProvider} to retrieve player instances from.
     */
    public TBNRMinigame(List<TBNRPlayer> players, Arena arena, GearzPlugin<TBNRPlayer, TBNRAbstractClass> plugin, GameMeta meta, Integer id, GearzPlayerProvider<TBNRPlayer> playerProvider) {
        super(players, arena, plugin, meta, id, playerProvider);
        this.pendingPoints = new HashMap<>();
    }

    @Override
    protected TBNRAbstractClass constructClassType(Class<? extends TBNRAbstractClass> classType, TBNRPlayer player) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return classType.getConstructor(TBNRPlayer.class, GearzGame.class).newInstance(player, this);
    }

    protected void addGPoints(TBNRPlayer player, Integer points) {
        this.pendingPoints.put(player, (this.pendingPoints.containsKey(player) ? this.pendingPoints.get(player) : 0) + points);
        player.getTPlayer().sendMessage(formatUsingMeta(getGameMeta(), TBNRNetworkManager.getInstance().getFormat("points-added", true, new String[]{"<points>", String.valueOf(points)})));
    }

    protected abstract int xpForPlaying();

    @Override
    public void onPlayerGameEnd(TBNRPlayer player, GameStopCause cause) {
        if (cause == GameStopCause.GAME_END) {
            if (!getAddedPlayers().contains(player)) {
                int points = 0;
                if (this.pendingPoints.containsKey(player)) {
                    points = this.pendingPoints.get(player);
                }
                player.addPoints(points);
                player.addXp(xpForPlaying());
                player.getTPlayer().sendMessage(formatUsingMeta(getGameMeta(), TBNRNetworkManager.getInstance().getFormat("xp-earned", true, new String[]{"<xp>", String.valueOf(xpForPlaying())})));
                player.getTPlayer().sendMessage(formatUsingMeta(getGameMeta(), TBNRNetworkManager.getInstance().getFormat("points-earned", true, new String[]{"<points>", String.valueOf(points)})));
            }
        } else {
            player.getTPlayer().sendMessage(TBNRNetworkManager.getInstance().getFormat("game-void"));
        }
        if (player.isValid()) {
            player.setHideStats(false);
        }
    }

    @Override
    public void onPlayerBecomePlayer(TBNRPlayer player) {
        this.pendingPoints.put(player, 0);
        player.setHideStats(true);
    }

    @Override
    public void onPlayerBecomeSpectator(TBNRPlayer player) {
        player.setHideStats(false);
    }
}

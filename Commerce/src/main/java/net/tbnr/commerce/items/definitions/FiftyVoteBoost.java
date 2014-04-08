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

package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.GearzCommerce;
import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.event.game.GameStartEvent;
import net.tbnr.gearz.game.voting.PlayerMapVoteEvent;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.manager.TBNRPlayer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

@CommerceItemMeta(
        key = "fifty_vote_boost",
        humanName = "Fifty Double Votes",
        tier = Tier.Standard,
        item = Material.MAP
)
public final class FiftyVoteBoost extends CommerceItem {
    private Integer votesLeft = null;
    private boolean usedVote = false;
    private static final String storeKey = "votes_remain";

    public FiftyVoteBoost(TBNRPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVote(PlayerMapVoteEvent event) {
        if (!event.getPlayer().equals(getPlayer())) return;
        event.setNumberOfVotes(event.getNumberOfVotes()*2);
        usedVote = true;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGameStart(GameStartEvent event) {
        if (!event.getGame().getPlayers().contains(getPlayer())) return;
        if (!usedVote) return;
        getPlayer().getTPlayer().sendMessage(GearzCommerce.getInstance().getFormat("formats.double-vote"));
        votesLeft--;
        if (votesLeft <= 0) {
            revoke();
        }
        setObject(storeKey, votesLeft);
    }

    @Override
    public void onRegister() {
        if (votesLeft == null) votesLeft = getObject(storeKey, Integer.class);
    }
    @Override
    public void onPurchase() {
        votesLeft = setObject(storeKey, 50);
    }
}

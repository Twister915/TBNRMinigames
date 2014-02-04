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
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

@CommerceItemMeta(
        key = "fifty_vote_boost",
        humanName = "Fifty Double Votes",
        tier = Tier.Standard
)
public final class FiftyVoteBoost extends CommerceItem {
    private Integer votesLeft = null;
    private boolean usedVote = false;
    private static final String storeKey = "votes_remain";

    public FiftyVoteBoost(GearzPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVote(PlayerMapVoteEvent event) {
        if (!event.getPlayer().equals(getPlayer())) return;
        event.setNumberOfVotes(event.getNumberOfVotes()*2);
        event.getPlayer().getTPlayer().sendMessage(GearzCommerce.getInstance().getFormat("formats.double-vote"));
        usedVote = true;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGameStart(GameStartEvent event) {
        if (!event.getGame().getPlayers().contains(getPlayer())) return;
        if (!usedVote) return;
        votesLeft--;
        if (votesLeft <= 0) {
            revoke();
        }
    }

    @Override
    public void onRegister() {
        if (votesLeft == null) votesLeft = getObject(storeKey, Integer.class);
    }
    @Override
    public void onPurchase() {
        votesLeft = setObject(storeKey, 50);
    }
    @Override
    public void onDeregister() {
        setObject(storeKey, votesLeft);
    }
}

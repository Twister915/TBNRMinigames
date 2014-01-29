package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.GearzCommerce;
import net.tbnr.commerce.items.CommerceItem;
import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.GearzException;
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
    private static final String storeKey = "votes_remain";

    public FiftyVoteBoost(GearzPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVote(PlayerMapVoteEvent event) {
        event.setNumberOfVotes(event.getNumberOfVotes()*2);
        event.getPlayer().getTPlayer().sendMessage(GearzCommerce.getInstance().getFormat("formats.double-vote"));
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
        votesLeft = setObjectInDB(storeKey, 50);
    }
    @Override
    public void onDeregister() {
        setObjectInDB(storeKey, votesLeft);
    }
}

package net.tbnr.commerce.items;

import com.mongodb.BasicDBList;
import net.tbnr.gearz.player.GearzPlayer;

import java.util.List;

/**
 *
 * CommerceItemAPI is the interface that represents the safe methods to call from other plugins to interact with the Item
 * module in {@link net.tbnr.commerce.GearzCommerce}
 *
 * It is also heavily documented as it is supposed to serve as an API.
 *
 */
public interface CommerceItemAPI {
    /**
     *
     * Stores a purchase of a {@link net.tbnr.commerce.items.CommerceItem} subtype for a {@link net.tbnr.gearz.player.GearzPlayer}.
     *
     * Use Case: When a player can purchase an item using their Tier and Points in the shop, this method should be called.
     *
     * This will not validate anything with currency or prices.
     *
     * @param player The {@link net.tbnr.gearz.player.GearzPlayer} who has made this purchase.
     * @param clazz The {@link net.tbnr.commerce.items.CommerceItem} class that relates to this purchase.
     */
    public void givePlayerItem(GearzPlayer player, Class<? extends CommerceItem> clazz);

    /**
     *
     * Checks if a {@link net.tbnr.gearz.player.GearzPlayer} has purchased an item in the past.
     *
     * @param player The {@link net.tbnr.gearz.player.GearzPlayer} who we are checking.
     * @param clazz The {@link net.tbnr.commerce.items.CommerceItem} class that relates to this search.
     *
     * @return If the player has purchased the item.
     */
    public boolean playerHasItem(GearzPlayer player, Class<? extends CommerceItem> clazz);

    /**
     *
     * Takes the "key" and searches all commerce items for any matching item classes
     *
     * @param key The key to search for (based on {@link net.tbnr.commerce.items.CommerceItemMeta}'s key variable).
     *
     * @return The matching class. Can be used in other API methods.
     */
    public Class<? extends CommerceItem> getItemForID(String key);

    /**
     * @return All of the CommerceItem Classes that are registered.
     */
    public List<Class<? extends CommerceItem>> getCommerceItems();

    /**
     *
     * Will return the instance of the CommerceItemMeta annotation that is related to the {@link net.tbnr.commerce.items.CommerceItem} class passed.
     *
     * @param clazz The {@link net.tbnr.commerce.items.CommerceItem} class that you are getting the meta for.
     *
     * @return The {@link net.tbnr.commerce.items.CommerceItem}'s {@link net.tbnr.commerce.items.CommerceItemMeta}.
     */
    public CommerceItemMeta getMetaFor(Class<? extends CommerceItem> clazz) ;

    /**
     *
     * This will "reload" a player.
     *
     *  Reloading is defined as searching through the database for all previous purchases a player has made, and then
     *  creating instances of {@link net.tbnr.commerce.items.CommerceItem}s for each purchase. This will also register
     *  events for each of those items.
     *
     * @param player The {@link net.tbnr.gearz.player.GearzPlayer} to reload purchases for.
     */
    @SuppressWarnings("unchecked")
    public void reloadPlayer(GearzPlayer player, Class<? extends CommerceItem>... recentlyPurchased);

    /**
     *
     */
    public void reloadPlayers();

    public BasicDBList getPurchaseList(GearzPlayer player);

    /**
     *
     * @param player
     * @param item
     */
    public void revokeItem(GearzPlayer player, CommerceItem item);

    /**
     *
     * @param player
     * @param item
     */
    public void revokeItem(GearzPlayer player, Class<? extends CommerceItem> item);

    /**
     *
     * @param player
     * @return
     */
    public List<CommerceItem> getItemsFor(GearzPlayer player);

    /**
     *
     * @param player
     * @param tier
     * @return
     */
    public boolean canUseTier(GearzPlayer player, Tier tier);

    /**
     *
     * @param player
     * @param item
     * @return
     */

    /*
        HIGH LEVEL API
        SHOULD BE USED FOR ACTUAL IMPLEMENTATION.
     */
    public boolean canPurchaseItem(GearzPlayer player, Class<? extends CommerceItem> item);

    public boolean purchaseItem(GearzPlayer player, Class<? extends CommerceItem> item);

    public boolean canPurchaseTier(GearzPlayer player, Tier tier);

    public boolean purchaseTier(GearzPlayer player, Tier tier);

}

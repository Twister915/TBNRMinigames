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
@SuppressWarnings("unused")
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
     * This will reload all of the players currently online.
     *
     * Read the description for the {@link #reloadPlayer(net.tbnr.gearz.player.GearzPlayer, Class[])} for more details on what "reloading" is.
     *
     */
    public void reloadPlayers();

    /**
     *
     * This will return the raw {@link BasicDBList} for a player. This will contain an array of {@link com.mongodb.BasicDBObject}s that all represent
     * purchases the player has made. These purchases are stored at base with two key->value entries in the {@link com.mongodb.BasicDBObject}.
     *
     * The "key" which will instruct the reader in the reload method to load a certain commerce item for this purchase
     * The "date_time" which will give a record of when the purchase was made.
     *
     * @param player The player to grab this data for
     *
     * @return The {@link BasicDBList} for this player.
     */
    public BasicDBList getPurchaseList(GearzPlayer player);

    /**
     *
     * Revoking an item will remove the item from the player immediately, and remove the item from the database.
     *
     * This is useful if the purchase you are creating expires in any way,
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

    /**
     *
     * @param player
     * @param item
     * @return
     */
    public void testItemPurchase(GearzPlayer player, Class<? extends CommerceItem> item) throws PurchaseException;

    /**
     *
     * @param player
     * @param item
     * @return
     */
    public void purchaseItem(GearzPlayer player, Class<? extends CommerceItem> item, PurchaseMethod method) throws PurchaseException;

    /**
     * Check's whether a player can purchase a tier
     * @param player ~ The Player to Check
     * @param tier ~ Tier to purchase
     * @return boolean ~ if player can purchase tier
     */
    public boolean canPurchaseTier(GearzPlayer player, Tier tier);

    /**
     * Purchases a tier for the player
     * @param player ~ The Player to purchase the tier
     * @param tier ~ The tier to buy
     * @return boolean ~ if it succeeded
     */
    public boolean purchaseTier(GearzPlayer player, Tier tier);

    /**
     * Check If Player has a tier
     * @param player ~ The Player to Check
     * @param tier ~ the tier to check
     * @return boolean ~ if player has tier
     */
    public boolean hasTier(GearzPlayer player, Tier tier);

    /**
     * Used for an argument for payment method choice.
     */
    public static enum PurchaseMethod {
        /**
         * Use standard free points.
         */
        Points,
        /**
         * Use donor credits.
         */
        Donor
    }
}

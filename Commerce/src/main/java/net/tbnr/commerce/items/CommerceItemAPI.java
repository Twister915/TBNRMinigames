package net.tbnr.commerce.items;

import net.tbnr.gearz.player.GearzPlayer;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Created by Joey on 1/26/14.
 */
public interface CommerceItemAPI {
    public void givePlayerItem(GearzPlayer player, Class<? extends CommerceItem> clazz);

    public boolean playerHasItem(GearzPlayer player, Class<? extends CommerceItem> clazz);

    public Class<? extends CommerceItem> getItemForID(String key);

    public List<Class<? extends CommerceItem>> getCommerceItems();

    public Annotation getMetaFor(Class<? extends CommerceItem> clazz) ;

    public void reloadPlayer(GearzPlayer player);

    public void reloadPlayers();

    public void revokeItem(GearzPlayer player, CommerceItem item);

    public void revokeItem(GearzPlayer player, Class<? extends CommerceItem> item);

    public List<CommerceItem> getItemsFor(GearzPlayer player);
}

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

package net.tbnr.commerce.items;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import lombok.Data;
import lombok.ToString;
import net.tbnr.commerce.GearzCommerce;
import net.tbnr.gearz.GearzException;
import net.tbnr.manager.TBNRNetworkManager;
import net.tbnr.manager.TBNRPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.lang.annotation.Annotation;

@Data
@ToString
public abstract class CommerceItem implements Listener {

    private final TBNRPlayer player;
    private final CommerceItemMeta meta;
    private final DBObject playerDoc;
    private final CommerceItemAPI api;

    public CommerceItem(TBNRPlayer player, CommerceItemAPI api) throws GearzException {
        Annotation[] declaredAnnotations = getClass().getDeclaredAnnotations();
        CommerceItemMeta meta = null;
        for (Annotation declaredAnnotation : declaredAnnotations) {
            if (declaredAnnotation instanceof CommerceItemMeta) {
                meta = (CommerceItemMeta) declaredAnnotation;
                break;
            }
        }
        if (meta == null) throw new GearzException("Could not find an annotation");
        this.meta = meta;
        this.player = player;
        this.playerDoc = player.getTPlayer().getPlayerDocument();
        this.api = api;
    }
    @SuppressWarnings("UnusedDeclaration")
    public final void register() {
        GearzCommerce.getInstance().registerEvents(this);
        onRegister();
    }

    public void onPurchase() {}

    public void onRegister() {}

    public void onUnregister() {}

    public void onRevoke() {}

    public final void revoke() {
        GearzCommerce.getInstance().getItemAPI().revokeItem(player, this);
        GearzCommerce.getInstance().getLogger().info("Revoking " + this.meta.humanName() + " from player " + this.player.getUsername());
    }

    public GearzCommerce getPlugin() { return GearzCommerce.getInstance();}

    /**
     * Takes advantage of the fact we use a BasicDBObject to store data about a purchase
     *
     * Use this to store data about a purchase, and be able to retrieve it.
     *
     * @param key The unique key for this information.
     * @param object The object to store
     * @param <T> The type of the object you're storing
     * @return The object you stored.
     */
    public final <T> T setObject(String key, T object) {
        if (key.equals("key")) throw new RuntimeException("You cannot use the key 'key'!");
        BasicDBList purchaseList = this.api.getPurchaseList(this.player);
        int index = 0;
        for (Object o : purchaseList) {
            if (!(o instanceof DBObject)) continue;
            DBObject item = (DBObject)o;
            if (!(item.get("key").equals(meta.key()))) {
                index++;
                continue;
            }
            item.put(key, object);
            purchaseList.set(index, item);
            playerDoc.put(CommerceItemManager.dbListKey, purchaseList);
            return object;
        }
        throw new RuntimeException("Could not find document for this commerce item!");
    }

    @SuppressWarnings({"unchecked", "UnusedParameters"})
    public final <T> T getObject(String key, Class<T> clazz) {
        BasicDBList purchaseList = this.api.getPurchaseList(this.player);
        for (Object o : purchaseList) {
            if (!(o instanceof DBObject)) continue;
            DBObject item = (DBObject)o;
            if (!(item.get("key").equals(meta.key()))) continue;
            T o1;
            try {
                o1 = (T) item.get(key);
            } catch (ClassCastException ex) {
                return null;
            }
            return o1;
        }
        throw new RuntimeException("Could not find document for this commerce item!");
    }

    protected TBNRPlayer resolveTbnrPlayer(Player player) {
        return TBNRNetworkManager.getInstance().getPlayerProvider().getPlayerFromPlayer(player);
    }
}

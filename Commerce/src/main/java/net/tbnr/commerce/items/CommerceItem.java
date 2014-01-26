package net.tbnr.commerce.items;

import lombok.Data;
import lombok.ToString;
import net.tbnr.commerce.GearzCommerce;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.player.GearzPlayer;
import org.bukkit.event.Listener;

import java.lang.annotation.Annotation;

@Data
@ToString
public abstract class CommerceItem implements Listener {

    private final GearzPlayer player;
    private final CommerceItemMeta meta;

    public CommerceItem(GearzPlayer player) throws GearzException {
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
    }
    @SuppressWarnings("UnusedDeclaration")
    public final void register() {
        GearzCommerce.getInstance().registerEvents(this);
    }

    public void purchased() {}

    public void registered() {}

    public final void revoke() {
        GearzCommerce.getInstance().getItemAPI().revokeItem(player, this);
    }

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
    public <T> T setObjectInDB(String key, T object) {
        //TODO save some data in the database for this object
        return object;
    }

    public <T> T getObject(String key, Class<T> clazz) {
        //TODO actually get data.
        return null;
    }
}

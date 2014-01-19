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
}

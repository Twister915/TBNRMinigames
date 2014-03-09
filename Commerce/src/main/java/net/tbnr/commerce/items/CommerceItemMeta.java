package net.tbnr.commerce.items;

import org.bukkit.Material;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * Annotate all items we're selling in ths shop with this.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommerceItemMeta {
    public String key();
    public String humanName();
    public Tier tier();
    public Material item() default Material.GRASS;
}

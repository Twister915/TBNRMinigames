package net.tbnr.commerce.items;

/**
 * Dictates a "conflict"
 *
 * If an item cannot be used while another on is present, this can be used to note that.
 *
 * Action performed:
 *
 */
public @interface Conflicts {
    public Class<? extends CommerceItem>[] value();
}

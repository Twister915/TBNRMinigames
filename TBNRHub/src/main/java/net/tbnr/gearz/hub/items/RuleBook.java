package net.tbnr.gearz.hub.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

/**
 * Created by rigor789 on 2013.12.21..
 */
public class RuleBook extends HubItem {

    public RuleBook() {
        super(false);
    }

    @Override
    public ItemStack getItem() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();

        meta.setTitle(ChatColor.RED + "TBNR RuleBook");
        meta.setPages(ChatColor.BLUE + "Welcome to TBNR, this is our rule book, please read it carefully!", ChatColor.GOLD + "SWAG.");
        meta.setAuthor("TBNR Server");
        book.setItemMeta(meta);
        return book;
    }
}

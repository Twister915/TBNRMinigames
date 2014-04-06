/*
 * Copyright (c) 2014.
 * Cogz Development LLC USA
 * All Right reserved
 *
 * This software is the confidential and proprietary information of Cogz Development, LLC.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with Cogz LLC.
 */

package net.tbnr.gearz.hub.items;

import net.tbnr.gearz.hub.annotations.HubItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rigor789 on 2013.12.21..
 *
 * Purpose Of File:
 *
 * Latest Change:
 */
public class RuleBook extends HubItem {

    public RuleBook() {
        super(false);
    }

    @Override
    public List<ItemStack> getItems() {

	    List<ItemStack> items = new ArrayList<>();
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();

        meta.setTitle(ChatColor.RED + "TBNR RuleBook");
        meta.setPages(ChatColor.BLUE + "Welcome to TBNR, this is our rule book, please read it carefully!", ChatColor.GOLD + "SWAG.");
        meta.setAuthor("TBNR Server");
        book.setItemMeta(meta);

	    items.add(book);

        return items;
    }
}

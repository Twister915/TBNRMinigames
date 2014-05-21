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

package net.tbnr.mc.commerce.items.definitions;

import net.cogzmc.engine.gearz.Gearz;
import net.cogzmc.engine.gearz.GearzException;
import net.tbnr.mc.commerce.items.CommerceItemAPI;
import net.tbnr.mc.commerce.items.CommerceItemMeta;
import net.tbnr.mc.commerce.items.Tier;
import net.tbnr.mc.manager.TBNRPlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@CommerceItemMeta(
        tier = Tier.Heroic,
        key = "proper_armor",
        humanName = "Proper Armor",
        item = Material.DIAMOND_CHESTPLATE
)
public final class ProperArmor extends AbstractArmorItem {

    public ProperArmor(TBNRPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    @SuppressWarnings("UnusedDeclaration")
    private static enum ArmorSets {
        LEATHER(new ArmorSet(Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS)),
        IRON(new ArmorSet(Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS)),
        GOLD(new ArmorSet(Material.GOLD_HELMET, Material.GOLD_CHESTPLATE, Material.GOLD_LEGGINGS, Material.GOLD_BOOTS)),
        DIAMOND(new ArmorSet(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS)),
        MIX_UP(new ArmorSet(Material.DIAMOND_HELMET, Material.GOLD_CHESTPLATE, Material.IRON_LEGGINGS, Material.LEATHER_LEGGINGS));

        private final ArmorSet set;

        ArmorSets(ArmorSet set) {
            this.set = set;
        }
        public ItemStack[] getArmorSet() {
            return set.getArmorSet();
        }
    }

    @Override
    protected ItemStack[] armorContents() {
        ArmorSets[] values = ArmorSets.values();
        return values[Gearz.getRandom().nextInt(values.length)].getArmorSet();
    }
}

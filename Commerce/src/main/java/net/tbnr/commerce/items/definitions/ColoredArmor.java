package net.tbnr.commerce.items.definitions;

import net.tbnr.commerce.items.CommerceItemAPI;
import net.tbnr.commerce.items.CommerceItemMeta;
import net.tbnr.commerce.items.Tier;
import net.tbnr.gearz.Gearz;
import net.tbnr.gearz.GearzException;
import net.tbnr.manager.TBNRPlayer;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

@CommerceItemMeta(
        key = "colored_armor",
        tier = Tier.Heroic,
        humanName = "Colored Armor",
        item = Material.LEATHER_CHESTPLATE
)
public class ColoredArmor extends AbstractArmorItem {
    private static final Material[] leatherArmor =
            new Material[]{Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS};

    public ColoredArmor(TBNRPlayer player, CommerceItemAPI api) throws GearzException {
        super(player, api);
    }

    @SuppressWarnings("UnusedDeclaration")
    private static enum ArmorSets {
        WHITE(Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE),
        RAINBOW(Color.RED, Color.YELLOW, Color.GREEN, Color.NAVY),
        CACTUS(Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN),
        FLAME(Color.RED, Color.ORANGE, Color.RED, Color.ORANGE),
        AQUA(Color.BLUE, Color.AQUA, Color.AQUA, Color.BLUE);


        private final Color[] color;

        ArmorSets(Color... set) {
            this.color = set;
        }
        public Color[] getColors() {
            return color;
        }
    }

    @Override
    protected ItemStack[] armorContents() {
        ArmorSets[] values = ArmorSets.values();
        Color[] colors = values[Gearz.getRandom().nextInt(values.length)].getColors();
        ItemStack[] stacks = new ItemStack[4];
        for (int x = stacks.length-1, y = 0; x > -1; x--, y++) {
            ItemStack itemStack = new ItemStack(leatherArmor[y]);
            LeatherArmorMeta itemMeta = (LeatherArmorMeta) itemStack.getItemMeta();
            itemMeta.setColor(colors[y]);
            itemStack.setItemMeta(itemMeta);
            stacks[x] = itemStack;
        }
        return stacks;
    }
}

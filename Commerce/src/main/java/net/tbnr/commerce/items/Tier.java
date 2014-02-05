package net.tbnr.commerce.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;

/**
 * Tier
 */
public enum  Tier {
    Standard("&8Standard", Material.GRASS, 2500, 90),
    Heroic("&7Heroic", Material.SKULL_ITEM, 5000, 180),
    Awesome("&6Awesome", Material.IRON_SWORD, 10000,360),
    Epic("&5Epic", Material.DIAMOND_SWORD, 20000,720),
    Iron_Veteran("&7&lIron Veteran", Material.IRON_INGOT, 7500, 500, 10, true),
    Golden_Veteran("&6&lGolden Veteran", Material.GOLD_INGOT, 15000, 700, 20, true, new Tier[]{Iron_Veteran}),
    Diamond_Veteran("&l&bDiamond Veteran", Material.DIAMOND, 30000, 900, 35, true, new Tier[] {Golden_Veteran});

    private final String humanName;
    private final Integer points;
    private final Integer donorCredits;
    private final Material repItem;
    private final boolean mustBePurchased;

    public Integer getRequiredLevel() {
        return requiredLevel;
    }

    private final Integer requiredLevel;

    private final Tier[] requires;

    public boolean isMustBePurchased() {
        return mustBePurchased;
    }

    public Tier[] getRequires() {
        return requires;
    }

    public String getHumanName() {
        return humanName;
    }

    public Integer getPoints() {
        return points;
    }

    public Integer getDonorCredits() {
        return donorCredits;
    }

    public Material getRepItem() {
        return this.repItem;
    }
    private Tier(String humanName, Material repItem, Integer points, Integer donorCredits, Integer level, boolean mustBePurchased, Tier[] requires) {
        this.humanName = ChatColor.translateAlternateColorCodes('&',humanName);
        this.repItem = repItem;
        this.points = points;
        this.donorCredits = donorCredits;
        this.requiredLevel = level;
        this.mustBePurchased = mustBePurchased;
        this.requires = requires;
    }

    private Tier(String humanName, Material repItem, Integer points, Integer donorCredits) {
        this(humanName, repItem, points, donorCredits, 0);
    }

    private Tier(String humanName, Material repItem, Integer points, Integer donorCredits, Integer level) {
            this(humanName, repItem, points, donorCredits, level, false);
    }

    private Tier(String humanName, Material repItem, Integer points, Integer donorCredits, Integer level, boolean mustBePurchased) {
            this(humanName, repItem, points, donorCredits, level, mustBePurchased, null);
    }

}

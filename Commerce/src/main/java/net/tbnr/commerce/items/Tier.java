package net.tbnr.commerce.items;

import org.bukkit.ChatColor;

/**
 * Tier
 */
public enum  Tier {
    Standard("&8Standard", 2500, 90),
    Heroic("&7Heroic", 5000, 180),
    Awesome("&6Awesome", 10000,360),
    Epic("&5Epic", 20000,720),
    Iron_Veteran("&7&lIron Veteran", 7500, 500, 10, true),
    Golden_Veteran("&6&lGolden Veteran", 15000, 700, 20, true, new Tier[]{Iron_Veteran}),
    Diamond_Veteran("&&lbDiamond Veteran", 30000, 900, 35, true, new Tier[] {Golden_Veteran});

    public String getHumanName() {
        return humanName;
    }

    public Integer getPoints() {
        return points;
    }

    public Integer getDonorCredits() {
        return donorCredits;
    }

    private String humanName;
    private Integer points;
    private Integer donorCredits;

    public Integer getRequiredLevel() {
        return requiredLevel;
    }

    private Integer requiredLevel;

    private Tier[] requires;

    public boolean isMustBePurchased() {
        return mustBePurchased;
    }

    public Tier[] getRequires() {
        return requires;
    }

    private boolean mustBePurchased;

    private Tier(String humanName, Integer points, Integer donorCredits, Integer level, boolean mustBePurchased, Tier[] requires) {
        this.humanName = ChatColor.translateAlternateColorCodes('&',humanName);
        this.points = points;
        this.donorCredits = donorCredits;
        this.requiredLevel = level;
        this.mustBePurchased = mustBePurchased;
        this.requires = requires;
    }

    private Tier(String humanName, Integer points, Integer donorCredits) {
        this(humanName, points, donorCredits, 0);
    }

    private Tier(String humanName, Integer points, Integer donorCredits, Integer level) {
            this(humanName, points, donorCredits, level, false);
    }

    private Tier(String humanName, Integer points, Integer donorCredits, Integer level, boolean mustBePurchased) {
            this(humanName, points, donorCredits, level, mustBePurchased, null);
    }

}

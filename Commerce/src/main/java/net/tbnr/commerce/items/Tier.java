package net.tbnr.commerce.items;

/**
 * Tier
 */
public enum  Tier {
    Standard("Standard", 2500, 90),
    Heroic("Heroic", 5000, 180),
    Awesome("Awesome", 10000,360),
    Epic("Epic", 20000,720),
    Iron_Veteran("Iron Veteran", 7500, 500, 10, true),
    Golden_Veteran("Golden Veteran", 15000, 700, 20, true, new Tier[]{Iron_Veteran}),
    Diamond_Veteran("Diamond Veteran", 30000, 900, 35, true, new Tier[] {Golden_Veteran});

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
        this.humanName = humanName;
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

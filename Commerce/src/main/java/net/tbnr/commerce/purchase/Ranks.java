package net.tbnr.commerce.purchase;

public enum Ranks {
    SUPPORTER("Supporter"),
    PREMIUM("Premium"),
    DONATOR("Donator"),
    HERO("Hero"),
    CHAMPION("Champion"),
    TBNR("TBNR"),
    CONTRIBUTOR("Contributor");
    private final String zPermsGroup;
    public String getzPermsGroup() {
        return zPermsGroup;
    }
    Ranks(String zPermsGroup) {
        this.zPermsGroup = zPermsGroup;
    }
}

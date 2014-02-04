package net.tbnr.commerce.items.shop;

public enum GuiKey {
    Shop("shop"),
    Tier("tier"),
    Main("main");

    private String key;

    GuiKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}

package net.tbnr.commerce.purchase;

/**
 * Created by Joey on 1/22/14.
 */
public enum Length {
    DAY("24h"),
    WEEK("7d"),
    TWO_WEEKS("14d"),
    MONTH("1m"),
    THREE_MONTHS("3m"),
    SIX_MONTHS("6m"),
    TWELVE_MONTHS("12m"),
    LIFETIME(null);
    private String lengthkey;
    public String getLengthkey() {
        return lengthkey;
    }
    Length(String key) {
        this.lengthkey = key;
    }
}

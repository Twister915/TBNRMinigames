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

package net.tbnr.commerce.purchase;

/**
 * Created by Joey on 1/22/14.
 *
 * Purpose Of File:
 *
 * Latest Change:
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
    private final String lengthkey;
    public String getLengthkey() {
        return lengthkey;
    }
    Length(String key) {
        this.lengthkey = key;
    }
}

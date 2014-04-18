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

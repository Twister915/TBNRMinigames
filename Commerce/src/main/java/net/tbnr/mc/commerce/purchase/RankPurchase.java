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

package net.tbnr.mc.commerce.purchase;

import com.mongodb.DB;
import com.mongodb.DBObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cogzmc.engine.activerecord.BasicField;
import net.cogzmc.engine.util.player.TPlayerManager;
import net.tbnr.mc.commerce.GearzCommerce;

@EqualsAndHashCode(callSuper = false)
@Data
public final class RankPurchase extends Purchase {
    public RankPurchase() {
        super();
    }

    public RankPurchase(DB database) {
        super(database);
    }

    public RankPurchase(DB database, DBObject dBobject) {
        super(database, dBobject);
    }
    @BasicField private Ranks rank;
    @BasicField private Length length;
    @BasicField private boolean rankedUp;
    public void performRankup() {
        if (rankedUp) return;
        String command = "perm player "
                + TPlayerManager.getUsernameForID(this.getPlayer())
                + " setgroup "
                + this.rank.getzPermsGroup();
        if (this.length.getLengthkey() != null) command = command + " " + this.length.getLengthkey();
        GearzCommerce.getInstance().sendConsoleCommand(command);
        rankedUp = true;
        save();
    }
}

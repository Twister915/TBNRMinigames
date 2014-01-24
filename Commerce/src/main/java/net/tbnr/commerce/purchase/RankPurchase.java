package net.tbnr.commerce.purchase;

import com.mongodb.DB;
import com.mongodb.DBObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.tbnr.commerce.GearzCommerce;
import net.tbnr.gearz.activerecord.BasicField;
import net.tbnr.util.player.TPlayerManager;

@EqualsAndHashCode(callSuper = false)
@Data
@ToString
public class RankPurchase extends Purchase {
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

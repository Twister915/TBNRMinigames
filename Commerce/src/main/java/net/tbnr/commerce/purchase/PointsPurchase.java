package net.tbnr.commerce.purchase;

import com.mongodb.DB;
import com.mongodb.DBObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.tbnr.gearz.activerecord.BasicField;

@EqualsAndHashCode(callSuper = false)
@Data
@ToString
public class PointsPurchase extends Purchase {
    public PointsPurchase() {
        super();
    }

    public PointsPurchase(DB database) {
        super(database);
    }

    public PointsPurchase(DB database, DBObject dBobject) {
        super(database, dBobject);
    }

    @BasicField private Integer points;
}

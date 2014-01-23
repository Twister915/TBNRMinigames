package net.tbnr.commerce.purchase;

import com.mongodb.DB;
import com.mongodb.DBObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.tbnr.gearz.activerecord.BasicField;
import net.tbnr.gearz.activerecord.GModel;
import org.bson.types.ObjectId;

import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
@ToString
public class Purchase extends GModel {
    public Purchase() {
        super();
    }

    public Purchase(DB database) {
        super(database);
    }

    public Purchase(DB database, DBObject dBobject) {
        super(database, dBobject);
    }

    @BasicField private ObjectId player;
    @BasicField private Date purchaseDate = new Date();
}

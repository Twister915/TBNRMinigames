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

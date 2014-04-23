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

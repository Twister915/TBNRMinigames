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

package net.tbnr.manager.classes;

import net.tbnr.gearz.game.classes.GearzClassSystem;
import net.tbnr.manager.TBNRPlayer;

public final class TBNRClassSystem extends GearzClassSystem<TBNRPlayer, TBNRAbstractClass>{
    @SafeVarargs
    public TBNRClassSystem(Class<? extends TBNRAbstractClass>... classes) {
        this(new TBNRClassResolver(), classes);
    }

    @SafeVarargs
    public TBNRClassSystem(TBNRClassResolver classResolver, Class<? extends TBNRAbstractClass>... classes) {
        super(classes, classResolver);
        this.getClassResolver().setClassSystem(this);
    }
}

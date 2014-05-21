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

package net.tbnr.manager.classes;

import net.tbnr.gearz.game.GameMeta;
import net.tbnr.gearz.game.classes.GearzClassSystem;
import net.tbnr.manager.TBNRPlayer;
import net.tbnr.manager.classes.pass.ClassPassManager;

import java.util.List;

public final class TBNRClassSystem extends GearzClassSystem<TBNRPlayer, TBNRAbstractClass>{
    public TBNRClassSystem(GameMeta gameMeta, Class defaultClass, List<Class<? extends TBNRAbstractClass>> classes) {
        this(new TBNRClassResolver(new ClassPassManager<>(gameMeta.key()), defaultClass), defaultClass, classes);
    }

    public TBNRClassSystem(TBNRClassResolver classResolver, Class<? extends TBNRAbstractClass> defaultClass, List<Class<? extends TBNRAbstractClass>> classes) {
        super(classes, classResolver, defaultClass);
        this.getClassResolver().setClassSystem(this);
    }
}

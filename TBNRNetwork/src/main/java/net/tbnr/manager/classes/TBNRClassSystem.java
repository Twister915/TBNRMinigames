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

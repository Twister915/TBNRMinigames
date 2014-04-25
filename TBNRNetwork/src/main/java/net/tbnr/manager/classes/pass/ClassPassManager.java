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

package net.tbnr.manager.classes.pass;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import lombok.Data;
import lombok.NonNull;
import net.tbnr.gearz.game.classes.GearzAbstractClass;
import net.tbnr.gearz.game.classes.GearzClassMeta;
import net.tbnr.manager.TBNRNetworkManager;
import net.tbnr.manager.TBNRPlayer;
import net.tbnr.manager.classes.TBNRAbstractClass;

/**
 * One instance of this will be created per minigame plugin, and can be created using the key in the constructor.
 */
@Data
public final class ClassPassManager<AbstractClassType extends TBNRAbstractClass> {
    private static final String database_key = "class_pass";
    @NonNull private final String gameKey;

    private static GearzClassMeta getClassMeta(Class<? extends GearzAbstractClass> classType) {
        return classType.getAnnotation(GearzClassMeta.class);
    }

    private String computeKeyForCreditObject(String classKey) {
        return "credits_" + gameKey + "_" + classKey;
    }

    public Integer getClassCreditsFor(Class<? extends AbstractClassType> classType, TBNRPlayer player) {
        DBObject creditsObject = getClassesDataObject(player);
        Integer credits = (Integer) creditsObject.get(computeKeyForCreditObject(getClassMeta(classType).key()));
        if (credits == null) credits = 0;
        return credits;
    }

    public void addClassCreditsFor(Integer credits, String classKey, TBNRPlayer player) {
        DBObject creditsObject = getClassesDataObject(player);
        String key = computeKeyForCreditObject(classKey);
        Integer currentCredits = (Integer) creditsObject.get(key);
        Integer newCredits = Math.max(0,currentCredits == null ? 0 : currentCredits + credits);
        creditsObject.put(key, newCredits);
        saveCreditsObject(player, creditsObject);
    }

    public void addClassCreditsFor(Integer credits, Class<? extends AbstractClassType> classType, TBNRPlayer player) {
        addClassCreditsFor(credits, getClassMeta(classType).key(), player);
    }

    public void setLastUsedClass(TBNRPlayer player, Class<? extends AbstractClassType> classType) {
        DBObject classesDataObject = getClassesDataObject(player);
        classesDataObject.put(gameKey + "_last_used_fqcn", classType.getName());
        saveCreditsObject(player, classesDataObject);
    }

    public Class<? extends AbstractClassType> getLastUsedClassFor(TBNRPlayer player) {
        DBObject creditsObject = getClassesDataObject(player);
        String fullyQualifiedClassnameForLastClass = (String) creditsObject.get(gameKey + "_last_used_fqcn");
        if (fullyQualifiedClassnameForLastClass == null) return null;
        Class<? extends AbstractClassType> classForClass;
        try {
            //noinspection unchecked
            classForClass = (Class<? extends AbstractClassType>) Class.forName(fullyQualifiedClassnameForLastClass);
        } catch (ClassNotFoundException | ClassCastException e) {
            return null;
        }
        if (getClassMeta(classForClass) == null) return null;
        return classForClass;
    }

    private DBObject getClassesDataObject(TBNRPlayer player) {
        DBObject storable = player.getTPlayer().getStorable(TBNRNetworkManager.getInstance(), database_key, DBObject.class);
        return storable == null ? new BasicDBObject() : storable;
    }

    private void saveCreditsObject(TBNRPlayer player, DBObject object) {
        player.getTPlayer().store(TBNRNetworkManager.getInstance(), database_key, object);
    }

    public void removeClassCreditsFor(Integer credits, Class<? extends AbstractClassType> classType, TBNRPlayer player) {
        addClassCreditsFor(credits * -1, classType, player);
    }

    /* Public methods for high level usage */

    public Class<? extends AbstractClassType> getClassForGame(TBNRPlayer player) {
        Class<? extends AbstractClassType> lastUsedClassFor = getLastUsedClassFor(player);
        if (lastUsedClassFor == null) return null;
        if (getClassCreditsFor(lastUsedClassFor, player) > 0) return lastUsedClassFor;
        else return null;
    }

    public void playerHasUsedClass(Class<? extends AbstractClassType> clazz, TBNRPlayer player) {
        removeClassCreditsFor(1, clazz, player);
        setLastUsedClass(player, clazz);
    }
}

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

package net.tbnr.minigame.sg;

import lombok.Getter;
import net.tbnr.gearz.GearzException;
import net.tbnr.manager.TBNRPlugin;
import org.bukkit.Bukkit;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: Joey
 * Date: 12/16/13
 * Time: 10:38 AM
 * To change this template use File | Settings | File Templates.
 */
public final class GSurvivalGames extends TBNRPlugin {
    @Getter
    private static GSurvivalGames instance;

    @Override
    public void enable() {
        GSurvivalGames.instance = this;
        try {
            registerGame(GSurvivalGamesArena.class, GSurvivalGamesGame.class);
        } catch (GearzException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private String getFileData(String filename) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getResource(filename)));
        String line;
        StringBuilder builder = new StringBuilder();
        try {
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return builder.toString();
    }

    public JSONObject getJSONResource(String resource) {
        JSONObject object;
        try {
            object = new JSONObject(getFileData(resource));
        } catch (JSONException e) {
            return null;
        }
        return object;
    }

    @Override
    public String getStorablePrefix() {
        return "survival_games";
    }
}

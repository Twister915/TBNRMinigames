package net.tbnr.gearz.hh;

import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.GearzPlugin;

/**
 * Created with IntelliJ IDEA.
 * User: Joey
 * Date: 12/12/13
 * Time: 10:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class GHeadHunter extends GearzPlugin {
    @Override
    public void enable() {
        try {
            registerGame(HeadHunterArena.class, HeadHunterGame.class);
        } catch (GearzException e) {
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public String getStorablePrefix() {
        return "headhunter";  //To change body of implemented methods use File | Settings | File Templates.
    }
}

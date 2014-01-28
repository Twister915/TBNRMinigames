package net.tbnr.gearz.pl.GPlague;

import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.GearzPlugin;

/**
 * Created by George on 27/01/14.
 * <p/>
 * Purpose Of File:
 * <p/>
 * Latest Change:
 */
public class GPlague extends GearzPlugin {
	@Override
	public void enable() {
		try {
			registerGame(PlagueArena.class, PlagueGame.class);
		} catch (GearzException e) {
			e.printStackTrace();
			getServer().getPluginManager().disablePlugin(this);
		}
	}

	@Override
	public String getStorablePrefix() {
		return "plague";
	}
}

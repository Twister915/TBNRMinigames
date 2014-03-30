package net.tbnr.gearz.pl;

import lombok.Getter;
import net.tbnr.gearz.GearzException;
import net.tbnr.gearz.GearzPlugin;
import net.tbnr.manager.TBNRPlugin;

/**
 * Created by George on 27/01/14.
 * <p/>
 * Purpose Of File:
 * <p/>
 * Latest Change:
 */
public class GPlague extends TBNRPlugin {

	@Getter static GearzPlugin instance;

	@SuppressWarnings("AccessStaticViaInstance")
	@Override
	public void enable() {
		this.instance = this;
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

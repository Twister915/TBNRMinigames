package net.tbnr.manager;

import lombok.Getter;
import net.tbnr.util.TPlugin;

public class TBNRNetworkManager extends TPlugin {
    @Getter private TBNRPlayerProvider playerProvider;
    @Getter private static TBNRNetworkManager instance;

    @Override
    public void enable() {
        TBNRNetworkManager.instance = this;
        this.playerProvider = new TBNRPlayerProvider();
    }

    @Override
    public void disable() {

    }

    @Override
    public String getStorablePrefix() {
        return null;
    }
}

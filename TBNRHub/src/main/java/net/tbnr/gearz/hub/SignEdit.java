package net.tbnr.gearz.hub;

import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

import java.util.HashMap;

/**
 * Created by rigor789 on 2014.01.10..
 */
public class SignEdit implements Listener {

    private HashMap<String, Sign> players;

    public SignEdit(){
        this.players = new HashMap<>();
    }

    @EventHandler
    public void onSignPlace(BlockPlaceEvent event){
        if(!(event.getBlockPlaced().getState() instanceof Sign)) return;
        if(!(event.getBlockAgainst().getState() instanceof Sign)) return;
        Sign sign = (Sign) event.getBlockAgainst().getState();
        Sign gui = (Sign) event.getBlockPlaced().getState();
        for(int i = 0; i < sign.getLines().length; i++){
            gui.setLine(i, sign.getLine(i));
        }
        this.players.put(event.getPlayer().getName(), sign);
    }

    @EventHandler
    public void onSignEdit(SignChangeEvent event){
        if(!this.players.containsKey(event.getPlayer().getName())) return;
        Sign sign = players.get(event.getPlayer());
        for(int i = 0; i < event.getLines().length; i++){
            sign.setLine(i, event.getLine(i));
        }
        this.players.remove(event.getPlayer().getName());
    }

}

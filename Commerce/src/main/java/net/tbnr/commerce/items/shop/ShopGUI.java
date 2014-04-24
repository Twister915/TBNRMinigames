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

package net.tbnr.commerce.items.shop;

import lombok.Getter;
import net.tbnr.gearz.player.GearzPlayer;
import net.tbnr.util.inventory.base.BaseGUI;
import net.tbnr.util.inventory.base.GUICallback;
import net.tbnr.util.inventory.base.GUIItem;

import java.util.ArrayList;

/**
 * Created by rigor789 on 2013.12.23..
 *
 * Purpose Of File:
 *
 * Latest Change:
 */
public class ShopGUI extends BaseGUI {
    /**
     *Player
     */
    @Getter private GearzPlayer player;

    /**
     * An InventoryGUI with callbacks and effects on
     *
     * @param items    And array list of the items to be put in the GUI
     * @param title    The title of the GUI
     * @param callback The callback that handles the clicks.
     */
    public ShopGUI(ArrayList<GUIItem> items, String title, GUICallback callback, GearzPlayer player) {
        this(items, title, callback, true, player);
    }

    /**
     * An InventoryGUI with callbacks
     *
     * @param items    And array list of the items to be put in the GUI
     * @param title    The title of the GUI
     * @param callback The callback that handles the clicks.
     * @param effects  Whether to show or not the effects
     */
    public ShopGUI(ArrayList<GUIItem> items, String title, GUICallback callback, boolean effects, GearzPlayer player) {
        super(items, title, callback, effects);
        this.player = player;
    }
}

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

/**
 * Created with IntelliJ IDEA.
 * User: Joey
 * Date: 2/4/14
 * Time: 11:09 AM
 * To change this template use File | Settings | File Templates.
 */
public interface PlayerShop {
    public void open();
    public void close();
    public GuiKey getCurrentGuiPhase();
    public void openGui(GuiKey key);
}

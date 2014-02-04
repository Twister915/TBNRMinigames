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

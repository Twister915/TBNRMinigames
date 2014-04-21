package net.tbnr.manager.classes.pass;

import lombok.Data;
import lombok.NonNull;
import net.tbnr.gearz.game.classes.GearzClassMeta;
import net.tbnr.manager.TBNRNetworkManager;
import net.tbnr.manager.TBNRPlayer;
import net.tbnr.manager.classes.ClassPassGUIItem;
import net.tbnr.manager.classes.HideClassFromGUI;
import net.tbnr.manager.classes.TBNRAbstractClass;
import net.tbnr.util.inventory.InventoryGUI;
import net.tbnr.util.inventory.base.BaseGUI;
import net.tbnr.util.inventory.base.GUICallback;
import net.tbnr.util.inventory.base.GUIItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Data
public final class ClassPickerGUI implements GUICallback {
    private final TBNRPlayer player;
    private final List<Class<? extends TBNRAbstractClass>> classes;
    @NonNull private final ClassPassManager<TBNRAbstractClass> classPassManager;

    private InventoryGUI inventoryGUI;
    private final Map<GUIItem, Class<? extends TBNRAbstractClass>> classesToItem = new HashMap<>();

    void postConstructorInit() {
        this.inventoryGUI = new InventoryGUI(generateGUIItems(), TBNRNetworkManager.getInstance().getFormat("formats.class-pass-item-name"), this, false);
    }

    public static ClassPickerGUI preparePickerGuiFor(TBNRPlayer player, List<Class<? extends TBNRAbstractClass>> classes, ClassPassManager<TBNRAbstractClass> classPassManager) {
        ClassPickerGUI classPickerGUI = new ClassPickerGUI(player, classes, classPassManager);
        classPickerGUI.postConstructorInit();
        return classPickerGUI;
    }

    private ArrayList<GUIItem> generateGUIItems() {
        ArrayList<GUIItem> items = new ArrayList<>();
        Class<? extends TBNRAbstractClass> lastUsedClassFor = classPassManager.getLastUsedClassFor(player);
        for (Class<? extends TBNRAbstractClass> aClass : classes) {
            if (aClass.isAnnotationPresent(HideClassFromGUI.class)) continue;
            GearzClassMeta classMeta = aClass.getAnnotation(GearzClassMeta.class);
            ClassPassGUIItem itemDefinition = aClass.getAnnotation(ClassPassGUIItem.class);
            GUIItem guiItem = itemDefinition != null
                    ? new GUIItem(new ItemStack(itemDefinition.material()), itemDefinition.title(), Arrays.asList(itemDefinition.lore()))
                    : new GUIItem(new ItemStack(Material.PAPER), classMeta.name(), Arrays.asList(classMeta.description()));
            if (lastUsedClassFor != null && lastUsedClassFor.equals(aClass)) guiItem.getItem().addUnsafeEnchantment(Enchantment.SILK_TOUCH, 32);
            guiItem.getItem().setAmount(classPassManager.getClassCreditsFor(aClass, player));
            classesToItem.put(guiItem, aClass);
            items.add(guiItem);
        }
        return items;
    }

    public void openGUI() {
        inventoryGUI.open(player.getPlayer());
    }

    public void closeGUI() {
        inventoryGUI.close(player.getPlayer());
    }

    @Override
    public void onItemSelect(BaseGUI gui, GUIItem item, Player player) {
        Class<? extends TBNRAbstractClass> aClass = classesToItem.get(item);
        TBNRPlayer player1 = resolveTBNRPlayer(player);
        if (classPassManager.getClassCreditsFor(aClass, player1) == 0) {
            this.player.sendMessage(TBNRNetworkManager.getInstance().getFormat("formats.class-no-credits"));
            this.player.getTPlayer().playSound(Sound.ANVIL_USE);
            return;
        }
        Class<? extends TBNRAbstractClass> lastUsedClassFor = classPassManager.getLastUsedClassFor(player1);
        if (lastUsedClassFor != null) {
            for (GUIItem guiItem : gui.getItems()) {
                if (classesToItem.get(guiItem).equals(lastUsedClassFor)) {
                    guiItem.getItem().removeEnchantment(Enchantment.SILK_TOUCH);
                    break;
                }
            }
        }
        classPassManager.setLastUsedClass(player1, aClass);
        for (Map.Entry<GUIItem, Class<? extends TBNRAbstractClass>> guiItemClassEntry : classesToItem.entrySet()) {
            if (guiItemClassEntry.getValue().equals(aClass)) {
                guiItemClassEntry.getKey().getItem().addUnsafeEnchantment(Enchantment.SILK_TOUCH, 32);
                break;
            }
        }
        player.sendMessage(TBNRNetworkManager.getInstance().getFormat("formats.class-chosen", false, new String[]{"<class-name>", aClass.getAnnotation(GearzClassMeta.class).name()}));
        player1.getTPlayer().playSound(Sound.NOTE_PLING);
    }

    @Override
    public void onGUIOpen(BaseGUI gui, Player player) {

    }

    @Override
    public void onGUIClose(BaseGUI gui, Player player) {

    }

    private TBNRPlayer resolveTBNRPlayer(Player player) {
        return TBNRNetworkManager.getInstance().getPlayerProvider().getPlayerFromPlayer(player);
    }
}

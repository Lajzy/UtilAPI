package me.lajzy.utilAPI.gui;

import me.lajzy.utilAPI.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class MenuBuilder {

    protected Inventory inventory;
    protected Player player;
    private final Map<Integer, Runnable> clickActions = new HashMap<>();

    public MenuBuilder(Player player, String title, int size) {
        this.player = player;
        this.inventory = Bukkit.createInventory(null, size, title);
        setup();
    }

    protected abstract void setup();

    public void open() {
        player.openInventory(inventory);
    }

    public void setItem(int slot, ItemStack item, Runnable action) {
        inventory.setItem(slot, item);
        clickActions.put(slot, action);
    }

    protected void fillEmptySlots(Material material) {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, new ItemBuilder(material).name(" ").build());
            }
        }
    }

    public void handleClick(InventoryClickEvent e) {
        e.setCancelled(true);
        Runnable action = clickActions.get(e.getSlot());
        if (action != null) action.run();
    }
}

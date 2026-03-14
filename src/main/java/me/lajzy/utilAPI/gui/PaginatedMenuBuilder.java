package me.lajzy.utilAPI.gui;

import me.lajzy.utilAPI.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class PaginatedMenuBuilder extends MenuBuilder {

    protected int page = 0;
    protected List<ItemStack> items;

    public PaginatedMenuBuilder(Player player, String title, int size) {
        super(player, title, size);
        this.items = items;
    }

    @Override
    protected void setup() {
        renderPage();
    }

    protected void renderPage() {
        inventory.clear();

        int start = page * 27; // visas 27 items per sida
        int end = Math.min(start + 27, items.size());

        for (int i = start; i < end; i++) {
            int index = i;
            int slot = i - start;
            ItemStack item = items.get(i);
            setItem(slot, item, () -> onItemClick(item, index));
        }



        if (page > 0) setItem(27, new ItemBuilder(Material.ARROW).name("&aPrevious").build(), () -> {
            page--;
            renderPage();
        });

        if (end < items.size()) setItem(35, new ItemBuilder(Material.ARROW).name("&aNext").build(), () -> {
            page++;
            renderPage();
        });
    }

    protected abstract void onItemClick(ItemStack item, int index);
}
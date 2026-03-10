package me.lajzy.utilAPI.Utils;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public final class CustomColor {

    private CustomColor() {}

    public static String color(@NotNull String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    @NotNull
    public static List<String> color(@NotNull List<String> txtList) {
        return txtList.stream()
                .map(CustomColor::color)
                .collect(Collectors.toList());
    }
}

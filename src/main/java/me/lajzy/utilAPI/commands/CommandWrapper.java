package me.lajzy.utilAPI.commands;

import me.lajzy.utilAPI.Utils.CustomColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CommandWrapper implements CommandExecutor, TabCompleter {

    private final String name;
    private String description = "";
    private String permission = null;
    private BiConsumer<CommandSender, String[]> executor;
    private final Map<String, CommandWrapper> subcommands = new HashMap<>();
    private BiConsumer<CommandSender, String[]> tabCompleter;
    private String permissionErrorMessage;

    public CommandWrapper(String name) {
        this.name = name.toLowerCase();
    }

    public CommandWrapper description(String desc) { this.description = desc; return this; }
    public CommandWrapper permission(String perm) { this.permission = perm; return this; }
    public CommandWrapper executor(BiConsumer<CommandSender, String[]> exec) { this.executor = exec; return this; }
    public CommandWrapper tabCompleter(BiConsumer<CommandSender, String[]> tab) { this.tabCompleter = tab; return this; }
    public CommandWrapper permissionError(String message) { this.permissionErrorMessage = message; return this; }

    // Subcommand
    public CommandWrapper sub(String name, BiConsumer<CommandSender, String[]> exec) {
        CommandWrapper sub = new CommandWrapper(name).executor(exec);
        subcommands.put(name.toLowerCase(), sub);
        return this;
    }

    // Register
    public void register(JavaPlugin plugin) {
        PluginCommand cmd = plugin.getCommand(name);
        if (cmd == null) {
            plugin.getLogger().warning("Command " + name + " is not defined in plugin.yml!");
            return;
        }
        cmd.setExecutor(this);
        cmd.setTabCompleter(this);
    }


    // Command & Subcommand
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (permission != null && !player.hasPermission(permission)) {
            player.sendMessage(CustomColor.color(permissionErrorMessage));
            return true;
        }

        if (args.length > 0) {
            CommandWrapper sub = subcommands.get(args[0].toLowerCase());
            if (sub != null) {
                sub.onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
                return true;
            }
        }

        if (executor != null) executor.accept(sender, args);
        return true;
    }

    // Tab completion
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            for (String sub : subcommands.keySet()) {
                if (sub.toLowerCase().startsWith(args[0].toLowerCase())) completions.add(sub);
            }
        } else if (args.length > 1) {
            CommandWrapper sub = subcommands.get(args[0].toLowerCase());
            if (sub != null && sub.tabCompleter != null) {
                sub.tabCompleter.accept(sender, Arrays.copyOfRange(args, 1, args.length));
            }
        }

        return completions;
    }
}

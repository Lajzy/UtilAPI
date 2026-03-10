package me.lajzy.utilAPI.commands;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private final List<CommandWrapper> commands = new ArrayList<>();

    public CommandManager add(CommandWrapper command) {
        commands.add(command);
        return this;
    }

    public void registerAll(JavaPlugin plugin) {
        for (CommandWrapper command : commands) {
            command.register(plugin);
        }
    }
}

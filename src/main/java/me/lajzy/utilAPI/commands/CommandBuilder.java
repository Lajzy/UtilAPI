package me.lajzy.utilAPI.commands;

public abstract class CommandBuilder {
    protected final CommandWrapper wrapper;

    public CommandBuilder(String name) {
        wrapper = new CommandWrapper(name);
        setup();
    }

    protected abstract void setup();

    public CommandWrapper getWrapper() {
        return wrapper;
    }
}

package com.paragon.client.features.command;

public abstract class Command {

    private String name;
    private String syntax;

    public Command(String name, String syntax) {
        this.name = name;
        this.syntax = syntax;
    }

    public abstract void whenCalled(String[] args);

    /**
     * Gets the command's name
     * @return The command's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the command's syntax
     * @return The command's syntax
     */
    public String getSyntax() {
        return syntax;
    }
}

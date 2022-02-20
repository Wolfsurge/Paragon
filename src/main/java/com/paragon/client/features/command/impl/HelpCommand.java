package com.paragon.client.features.command.impl;

import com.paragon.client.Paragon;
import com.paragon.client.features.command.Command;
import com.paragon.client.managers.CommandManager;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("Help", "help");
    }

    @Override
    public void whenCalled(String[] args, boolean fromConsole) {
        for (Command command : Paragon.commandManager.getCommands()) {
            CommandManager.sendClientMessage(command.getName(), fromConsole);
        }
    }
}

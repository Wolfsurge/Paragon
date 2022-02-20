package com.paragon.client.features.command.impl;

import com.paragon.client.Paragon;
import com.paragon.client.features.command.Command;
import com.paragon.client.managers.CommandManager;
import net.minecraft.util.text.TextFormatting;

public class SyntaxCommand extends Command {

    public SyntaxCommand() {
        super("Syntax", "syntax [command]");
    }

    @Override
    public void whenCalled(String[] args, boolean fromConsole) {
        if (args.length == 1) {
            for (Command command : Paragon.commandManager.getCommands()) {
                if (command.getName().equalsIgnoreCase(args[0])) {
                    CommandManager.sendClientMessage(command.getSyntax(), fromConsole);
                    break;
                }
            }
        } else {
            CommandManager.sendClientMessage(TextFormatting.RED + "Invalid syntax!", fromConsole);
        }
    }

}

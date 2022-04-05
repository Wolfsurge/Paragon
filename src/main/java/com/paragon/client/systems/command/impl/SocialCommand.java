package com.paragon.client.systems.command.impl;

import com.paragon.Paragon;
import com.paragon.api.util.render.TextRenderer;
import com.paragon.api.util.string.EnumFormatter;
import com.paragon.client.managers.CommandManager;
import com.paragon.client.managers.social.Player;
import com.paragon.client.managers.social.Relationship;
import com.paragon.client.systems.command.Command;
import net.minecraft.util.text.TextFormatting;

/**
 * @author Wolfsurge
 */
public class SocialCommand extends Command implements TextRenderer {

    public SocialCommand() {
        super("Social", "social [add/remove/list] [name] [add - relationship]");
    }

    public void whenCalled(String[] args, boolean fromConsole) {
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            // List all players
            if (Paragon.INSTANCE.getSocialManager().players.isEmpty()) {
                CommandManager.sendClientMessage(TextFormatting.RED + "You haven't added anyone to your social list!", fromConsole);
                return;
            }

            for (Player player : Paragon.INSTANCE.getSocialManager().players) {
                CommandManager.sendClientMessage(player.getName() + " - " + player.getRelationship().getTextFormatting() + player.getRelationship().toString(), fromConsole);
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("add")) {
            // Add a player
            try {
                String name = args[1];
                String relationship = args[2];
                Player player = new Player(name, Relationship.valueOf(relationship.toUpperCase()));

                Paragon.INSTANCE.getSocialManager().addPlayer(player);
                CommandManager.sendClientMessage(TextFormatting.GREEN + "Added player " + name + " to your socials list!", fromConsole);

                // Save social
                Paragon.INSTANCE.getStorageManager().saveSocial();
            } catch (IllegalArgumentException exception) {
                CommandManager.sendClientMessage(TextFormatting.RED + "Invalid argument! Should be 'friend', 'neutral', or 'enemy'", fromConsole);
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            // Remove a player
            String name = args[1];

            Paragon.INSTANCE.getSocialManager().removePlayer(name);
            CommandManager.sendClientMessage(TextFormatting.GREEN + "Removed player " + name + " from your socials list!", fromConsole);

            // Save social
            Paragon.INSTANCE.getStorageManager().saveSocial();
        } else {
            // Say that we have invalid syntax
            CommandManager.sendClientMessage(TextFormatting.RED + "Invalid Syntax!", fromConsole);
        }
    }

}
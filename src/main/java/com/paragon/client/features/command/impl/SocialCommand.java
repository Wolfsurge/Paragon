package com.paragon.client.features.command.impl;

import com.paragon.client.Paragon;
import com.paragon.client.features.command.Command;
import com.paragon.client.managers.CommandManager;
import com.paragon.client.managers.social.Player;
import com.paragon.client.managers.social.Relationship;
import net.minecraft.util.text.TextFormatting;

/**
 * @author Wolfsurge
 */
public class SocialCommand extends Command {

    public SocialCommand() {
        super("Social", "social [add/remove/list] [name] [add - relationship]");
    }

    public void whenCalled(String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            // List all players
            for (Player player : Paragon.socialManager.players) {
                CommandManager.sendClientMessage(player.getName() + " - " + player.getRelationship().toString());
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("add")) {
            // Add a player
            String name = args[1];
            String relationship = args[2];
            Player player = new Player(name, Relationship.valueOf(relationship.toUpperCase()));

            Paragon.socialManager.addPlayer(player);
            CommandManager.sendClientMessage(TextFormatting.GREEN + "Added player " + name + " to your friends list!");

            // Save social
            Paragon.storageManager.saveSocial();
        } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            // Remove a player
            String name = args[1];

            Paragon.socialManager.removePlayer(name);
            CommandManager.sendClientMessage(TextFormatting.GREEN + "Removed player " + name + " from your friends list!");

            // Save social
            Paragon.storageManager.saveSocial();
        } else {
            // Say that we have invalid syntax
            CommandManager.sendClientMessage(TextFormatting.RED + "Invalid Syntax!");
        }
    }

}

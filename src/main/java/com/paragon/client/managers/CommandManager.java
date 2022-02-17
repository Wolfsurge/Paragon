package com.paragon.client.managers;

import com.paragon.api.util.Wrapper;
import com.paragon.client.features.command.Command;
import com.paragon.client.features.command.impl.SocialCommand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Wolfsurge
 */
public class CommandManager implements Wrapper {

    public static String prefix = ">>";

    private ArrayList<Command> commands = new ArrayList<>();

    public CommandManager() {
        MinecraftForge.EVENT_BUS.register(this);

        commands.add(new SocialCommand());
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatEvent event) {
        // Check if the message starts with the prefix
        if (event.getMessage().startsWith(prefix)) {
            event.setCanceled(true);

            String message = event.getMessage().substring(prefix.length());

            if (message.split(" ").length > 0) {
                boolean commandFound = false;
                String commandName = message.split(" ")[0];

                for (Command command : commands) {
                    if (command.getName().equalsIgnoreCase(commandName)) {
                        command.whenCalled(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length));
                        commandFound = true;
                        break;
                    }
                }

                if (!commandFound) {
                    sendClientMessage(TextFormatting.RED + "Command not found!");
                }
            }
        }
    }

    public static void sendClientMessage(String message) {
        mc.player.sendMessage(new TextComponentString(TextFormatting.LIGHT_PURPLE + "Paragon " + TextFormatting.WHITE + "> " + message));
    }

}

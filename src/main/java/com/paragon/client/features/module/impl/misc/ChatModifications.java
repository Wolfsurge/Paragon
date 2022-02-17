package com.paragon.client.features.module.impl.misc;

import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.settings.impl.BooleanSetting;
import com.paragon.client.managers.CommandManager;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Wolfsurge
 */
public class ChatModifications extends Module {

    private final BooleanSetting coloured = new BooleanSetting("Coloured", "Adds a '>' before the message", false);
    private final BooleanSetting suffix = new BooleanSetting("Suffix", "Adds a Paragon suffix to the end of the message", true);

    public ChatModifications() {
        super("ChatModifications", "Changes the way you send messages", Category.MISC);
        this.addSettings(coloured, suffix);
    }

    @SubscribeEvent
    public void onChat(ClientChatEvent event) {
        if (event.getMessage().startsWith(CommandManager.prefix)) {
            return;
        }

        if (coloured.isEnabled()) {
            event.setMessage(">" + event.getMessage());
        }

        if (suffix.isEnabled()) {
            event.setMessage(event.getMessage() + " | Paragon");
        }
    }

}

package com.paragon.client.features.module.impl.combat;

import com.paragon.api.event.events.UpdateEvent;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.settings.impl.BooleanSetting;
import com.paragon.client.features.module.settings.impl.ModeSetting;
import com.paragon.client.features.module.settings.impl.NumberSetting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Wolfsurge
 * @since 31/01/22
 */
public class AutoCrystal extends Module {

    // Targeting
    private final NumberSetting targetRange = new NumberSetting("Target Range", "How far to find targets", 7, 1, 15, 1);
    private final ModeSetting targetPriority = new ModeSetting("Target Priority", "The mode to find targets", "Distance", new String[]{"Closest", "Health"});
    private final BooleanSetting reverseList = new BooleanSetting("Reverse Target List", "Reverse the targets list, to find the player with the highest value", false);

    // Placing
    private final BooleanSetting place = new BooleanSetting("Place", "Automatically place crystals", true);

    // Breaking
    private final BooleanSetting breakCrystals = new BooleanSetting("Break", "Break crystals around you", true);

    // Other
    private final ModeSetting updateEvent = new ModeSetting("Update", "When to call the update", "Both", new String[]{"Client", "Server", "Both"});
    private final ModeSetting logic = new ModeSetting("Logic", "Whether to break or place crystals before the other", "Place -> Break", new String[]{"Break -> Place", "Place -> Break"});

    // Variables
    private EntityPlayer currentTarget; // The current player being targeted

    public AutoCrystal() {
        super("AutoCrystal", "Automatically places and breaks crystals", Category.COMBAT);
        this.addSettings(targetRange, targetPriority, reverseList);
        this.addSettings(place);
        this.addSettings(breakCrystals);
        this.addSettings(updateEvent, logic);
    }

    @EventHandler
    private final Listener<UpdateEvent> updateEventListener = new Listener<>(event -> {
        if(updateEvent.is("Client") && event.getUpdateType() != UpdateEvent.Type.Client || updateEvent.is("Server") && event.getUpdateType() != UpdateEvent.Type.Server) return;

        doAutoCrystal();
    });

    public void doAutoCrystal() {
        switch (logic.getCurrentMode()) {
            case "Place -> Break":
                placeCrystals();
                breakCrystals();
                break;
            case "Break -> Place":
                breakCrystals();
                placeCrystals();
        }
    }

    public void breakCrystals() {
        if(breakCrystals.isEnabled()) {

        }
    }

    public void placeCrystals() {
        if(place.isEnabled()) {

        }
    }

    public EntityPlayer getBestTarget() {
        Stream<Entity> entityStream = mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityPlayer && entity.getDistance(mc.player) <= targetRange.getValue());
        List<Entity> playerEntities = entityStream.collect(Collectors.toList());
        return null;
    }
}

package com.paragon.client.managers;

import com.paragon.api.util.Wrapper;
import com.paragon.client.managers.rotation.Rotation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

/**
 * @author Wolfsurge
 */
public class RotationManager implements Wrapper {

    private ArrayList<Rotation> rotations = new ArrayList<>();

    public RotationManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!rotations.isEmpty() && !nullCheck()) {
            rotations.get(0).doRotation();
            rotations.remove(0);
        }
    }

    public void addRotation(Rotation rotation) {
        rotations.add(rotation);
    }

}

package com.paragon.client.features.module.impl.misc;

import com.mojang.authlib.GameProfile;
import com.paragon.api.util.player.EntityFakePlayer;
import com.paragon.api.util.render.RenderUtil;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.settings.impl.BooleanSetting;
import com.paragon.client.features.module.settings.impl.ColourSetting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.UUID;

/**
 * @author Wolfsurge
 */
public class FakePlayer extends Module {

    private final BooleanSetting tracer = new BooleanSetting("Tracer", "Draws a tracer to the fake player", true);
    private final ColourSetting tracerColour = (ColourSetting) new ColourSetting("Colour", "The colour of the tracer", new Color(255, 255, 255, 255)).setParentSetting(tracer);

    private EntityFakePlayer fakePlayer;

    public FakePlayer() {
        super("FakePlayer", "Spawns a client-side fake player to help with configging modules", Category.MISC);
        this.addSettings(tracer);
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            return;
        }

        fakePlayer = new EntityFakePlayer("Fake Player");
    }

    @Override
    public void onDisable() {
        if (fakePlayer == null) {
            return;
        }

        fakePlayer.despawn();
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (tracer.isEnabled() && fakePlayer != null) {
            RenderUtil.drawTracer(fakePlayer, 0.5f, tracerColour.getColour());
        }
    }
}

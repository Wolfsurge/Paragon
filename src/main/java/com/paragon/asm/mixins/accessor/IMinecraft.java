package com.paragon.asm.mixins.accessor;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface IMinecraft {

    @Accessor("session")
    void setSession(Session newSession);

}

package com.paragon.asm.mixins;

import com.paragon.api.event.events.RenderEntityEvent;
import com.paragon.client.Paragon;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderLivingBase.class, priority = Integer.MAX_VALUE)
public abstract class MixinRenderLivingBase<T extends EntityLivingBase> extends Render<T> {

    @Shadow
    protected ModelBase mainModel;

    protected MixinRenderLivingBase(RenderManager renderManager) { super(renderManager);}

    @Inject(method = "renderModel", at = @At("TAIL"))
    public void renderModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, CallbackInfo ci) {
        RenderEntityEvent renderEntityEvent = new RenderEntityEvent(mainModel, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        Paragon.EVENT_BUS.post(renderEntityEvent);
    }
}

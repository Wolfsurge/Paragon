package com.paragon.api.event.events;

import com.paragon.api.event.Event;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

/**
 * @author Wolfsurge
 * @since 2/2/22
 */
public class RenderEntityEvent extends Event {

    // The entity being rendered
    public Entity entity;

    // The model of the entity
    public ModelBase modelBase;

    // Required variables
    public float limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale;

    public RenderEntityEvent(ModelBase modelBase, Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.modelBase = modelBase;
        this.entity = entityIn;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.ageInTicks = ageInTicks;
        this.netHeadYaw = netHeadYaw;
        this.headPitch = headPitch;
        this.scale = scale;
    }

    /**
     * Gets the model base
     * @return The model base
     */
    public ModelBase getModelBase() {
        return modelBase;
    }

    /**
     * Gets the entity
     * @return The entity
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Gets the limb swing
     * @return The limb swing
     */
    public float getLimbSwing() {
        return limbSwing;
    }

    /**
     * Gets the limb swing amount
     * @return The limb swing amount
     */
    public float getLimbSwingAmount() {
        return limbSwingAmount;
    }

    /**
     * Gets the age in ticks
     * @return The age in ticks
     */
    public float getAgeInTicks() {
        return ageInTicks;
    }

    /**
     * Gets the net head yaw
     * @return The net head yaw
     */
    public float getNetHeadYaw() {
        return netHeadYaw;
    }

    /**
     * Gets the head pitch
     * @return The head pitch
     */
    public float getHeadPitch() {
        return headPitch;
    }

    /**
     * Gets the scale
     * @return The scale
     */
    public float getScale() {
        return scale;
    }

    /**
     * Renders the model
     */
    public void renderModel() {
        getModelBase().render(getEntity(), getLimbSwing(), getLimbSwingAmount(), getAgeInTicks(), getNetHeadYaw(), getHeadPitch(), getScale());
    }
}

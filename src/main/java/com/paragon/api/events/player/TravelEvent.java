package com.paragon.api.events.player;

import com.paragon.api.events.Event;

public class TravelEvent extends Event {

    private float strafe, vertical, forward;

    public TravelEvent(float strafe, float vertical, float forward) {
        this.strafe = strafe;
        this.vertical = vertical;
        this.forward = forward;
    }

    /**
     * Gets the strafe
     * @return The strafe
     */
    public float getStrafe() {
        return strafe;
    }

    /**
     * Sets the strafe
     * @param strafe The new strafe
     */
    public void setStrafe(float strafe) {
        this.strafe = strafe;
    }

    /**
     * Gets the vertical
     * @return The vertical
     */
    public float getVertical() {
        return vertical;
    }

    /**
     * Sets the vertical
     * @param vertical The new vertical
     */
    public void setVertical(float vertical) {
        this.vertical = vertical;
    }

    /**
     * Gets the forward
     * @return The forward
     */
    public float getForward() {
        return forward;
    }

    /**
     * Sets the forward
     * @param forward The new forward
     */
    public void setForward(float forward) {
        this.forward = forward;
    }
}

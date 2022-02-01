package com.paragon.api.event.events;

import com.paragon.api.event.Event;

/**
 * @author Wolfsurge
 * @since 31/01/22
 */
public class UpdateEvent extends Event {

    // What type of update it is
    private Type updateType;

    public UpdateEvent(Type updateType) {
        setUpdateType(updateType);
    }

    /**
     * Gets the update type
     * @return The update type
     */
    public Type getUpdateType() {
        return updateType;
    }

    /**
     * Sets the update type
     * @param updateType The new update type
     */
    public void setUpdateType(Type updateType) {
        this.updateType = updateType;
    }

    public enum Type {
        /**
         * Called when the client ticks
         */
        Client,

        /**
         * Called whem the server ticks
         */
        Server,

        /**
         * Called on both ticks
         */
        Both
    }

}
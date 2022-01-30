package com.paragon.api.event;

import me.zero.alpine.type.Cancellable;

/**
 * @author Wolfsurge
 */

public class Event extends Cancellable {

    /* The era of the event */
    private Era era;

    /**
     * An event with no parameters
     */
    public Event() {}

    /**
     * Initialise basic event
     * @param era The era of the event
     */
    public Event(Era era) {
        setEra(era);
    }

    /**
     * The era
     */
    public enum Era {
        // Before an event
        PRE,

        // After an event
        POST,

        // During an event
        PERI
    }

    /**
     * Gets the era of the event
     * @return The era of the event
     */
    public Era getEra() {
        return era;
    }

    /**
     * Sets the era of the event
     * @param era The new era
     */
    public void setEra(Era era) {
        this.era = era;
    }

}

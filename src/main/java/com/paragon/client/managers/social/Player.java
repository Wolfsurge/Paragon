package com.paragon.client.managers.social;

/**
 * @author Wolfsurge
 */
public class Player {

    // The player's name
    private String name;

    // What relationship we have with the player
    private Relationship relationship;

    public Player(String name, Relationship relationship) {
        this.name = name;
        this.relationship = relationship;
    }

    /**
     * Gets the player's name
     * @return The player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the relationship we have to the player
     * @return The relationship
     */
    public Relationship getRelationship() {
        return relationship;
    }
}

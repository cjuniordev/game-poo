package org.seariver.actor.entity;

public enum Directions {
    LEFT(-1), RIGHT(1);

    private final int direction;

    Directions(int value){
        this.direction = value;
    }
    
    public int getDirection(){
        return this.direction;
    }
}

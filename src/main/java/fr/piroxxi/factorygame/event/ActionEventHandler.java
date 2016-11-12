package fr.piroxxi.factorygame.event;

public abstract class ActionEventHandler<E extends ActionEvent> {
    public abstract void event(E event);
}

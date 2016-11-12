package fr.piroxxi.factorygame.core.exceptions;

public class CantPayException extends IllegalGameAction {
    public CantPayException(String message) {
        super(message);
    }
}

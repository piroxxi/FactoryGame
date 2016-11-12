package fr.piroxxi.factorygame.core.exceptions;

/**
 * Thrown when someone wants to start using a {@link fr.piroxxi.factorygame.core.model.Game} but
 * hasn't initialized it.
 */
public class UninitializedGameException extends RuntimeException {
    public UninitializedGameException(String message) {
        super(message);
    }
}

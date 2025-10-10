package me.simoncrafter.de.hamster.fsm.model.state;

/**
 * Exception, die ausgelöst wird, wenn bei der Ausführung mehrere Transitionen für einen Zustand gefunden
 * wurden, die ausführbar sind und der endliche Automat deterministisch.
 * @author Raffaela Ferrari
 *
 */
public class IsNondeterministicException extends Exception {
	
	/**
	 * Exception-Text
	 */
	private String message;
	
	/**
	 * Konstruktor
	 * @param stateName Name des Zustands, für den mehrere ausführbare Transitionen gefunden wurden.
	 */
	public IsNondeterministicException(String stateName) {
		this.message = "Es wurden f�r den Zustand " + stateName + " mehrere ausf�hrbare Transitionen" +
				" gefunden, obwohl der Automat als deterministisch eingestellt ist.";
	}

	/**
	 * Gibt den Exception-Text zurück
	 */
	public String getMessage() {
		return this.message;
    }
}

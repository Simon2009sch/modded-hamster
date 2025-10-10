package me.simoncrafter.de.hamster.fsm.controller.handler;

import java.awt.event.MouseEvent;

import me.simoncrafter.de.hamster.fsm.controller.FsmMenuMode;
import me.simoncrafter.de.hamster.fsm.view.FsmAutomataPanel;

/**
 * Diese Klasse kapselt im Controller die Reaktion auf Klicken auf den CreateStateMode-Button.
 * @author Raffaela Ferrari
 */
public class CreateStateHandler extends KlickListenerFsmAutomataMenu{

	/**
	 * Konstruktor
	 * @param con Controller, der die Benutzerinteraktion steuert
	 */
	public CreateStateHandler(FsmAutomataPanel con) {
		super(con);
	}

	@Override
	public void mousePressed(MouseEvent event) {
		// Controller weiß nun, dass er im CreateStateMode ist
		this.getController().setModeType(FsmMenuMode.createStateMode);
	}
}

package me.simoncrafter.de.hamster.fsm.model.transition.output;

import me.simoncrafter.de.hamster.fsm.controller.FsmProgram;
import me.simoncrafter.de.hamster.fsm.model.FsmObject;
import me.simoncrafter.de.hamster.fsm.model.transition.VoidObject;

/**
 * Klasse, die ein Vor-Operand im Output eines endlichen Automaten repräsentiert.
 * @author Raffaela Ferrari
 *
 */
public class VorObject extends VoidObject{

	/**
	 * Konstruktor
	 */
	public VorObject() {
		super("vor");
	}

	@Override
	public FsmObject clone() {
		VorObject clonedVorObject = new VorObject();
		clonedVorObject.setParent(this.parent);
		clonedVorObject.setCoordinates(this.xStart, this.yStart);
		return clonedVorObject;
	}

	@Override
	public Object performImplementation(FsmProgram program) {
		this.hamster.vor();
		return null;
	}
}

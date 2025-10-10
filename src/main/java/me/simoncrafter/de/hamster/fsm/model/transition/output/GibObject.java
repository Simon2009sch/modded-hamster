package me.simoncrafter.de.hamster.fsm.model.transition.output;

import me.simoncrafter.de.hamster.fsm.controller.FsmProgram;
import me.simoncrafter.de.hamster.fsm.model.FsmObject;
import me.simoncrafter.de.hamster.fsm.model.transition.VoidObject;

/**
 * Klasse, die ein Gib-Operand im Output eines endlichen Automaten repräsentiert.
 * @author Raffaela Ferrari
 *
 */
public class GibObject extends VoidObject{

	/**
	 * Konstruktor
	 */
	public GibObject() {
		super("gib");
	}

	@Override
	public FsmObject clone() {
		GibObject clonedGibObject = new GibObject();
		clonedGibObject.setParent(this.parent);
		clonedGibObject.setCoordinates(this.xStart, this.yStart);
		return clonedGibObject;
	}

	@Override
	public Object performImplementation(FsmProgram program) {
		this.hamster.gib();
		return null;
	}

}

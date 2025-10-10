package me.simoncrafter.de.hamster.fsm.model.transition.output;

import me.simoncrafter.de.hamster.fsm.controller.FsmProgram;
import me.simoncrafter.de.hamster.fsm.model.FsmObject;
import me.simoncrafter.de.hamster.fsm.model.transition.VoidObject;

/**
 * Klasse, die ein LinksUm-Operand im Output eines endlichen Automaten repräsentiert.
 * @author Raffaela Ferrari
 *
 */
public class LinksUmObject extends VoidObject{

	/**
	 * Konstruktor
	 */
	public LinksUmObject() {
		super("linksUm");
	}

	@Override
	public FsmObject clone() {
		LinksUmObject clonedLinksUmObject = new LinksUmObject();
		clonedLinksUmObject.setParent(this.parent);
		clonedLinksUmObject.setCoordinates(this.xStart, this.yStart);
		return clonedLinksUmObject;
	}

	@Override
	public Object performImplementation(FsmProgram program) {
		this.hamster.linksUm();
		return null;
	}
}

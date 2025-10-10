package me.simoncrafter.de.hamster.flowchart.model.decision;

import me.simoncrafter.de.hamster.flowchart.controller.FlowchartProgram;
import me.simoncrafter.de.hamster.flowchart.model.DecisionObject;

/**
 * PAP implementierung vom Hamster VornFrei-Befehl
 * 
 * @author gerrit
 * 
 */
public class VornFreiDecisionObject extends DecisionObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7618229330909859334L;
	private Boolean doNegate;

	public VornFreiDecisionObject(String decision, Boolean not) {
		super(decision);
		this.doNegate = not;
		if (not)
			this.setString("!" + this.getText());
		this.setType("decision");
		this.setPerform("vornFrei");
		this.setString("vornFrei?"); // dibo
	}

	@Override
	public Object executeImpl(FlowchartProgram program) {
		if (doNegate) {
			return !hamster.vornFrei();
		} else {
			return hamster.vornFrei();
		}

	}

}

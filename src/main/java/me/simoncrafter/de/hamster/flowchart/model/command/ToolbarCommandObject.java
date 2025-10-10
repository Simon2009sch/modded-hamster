package me.simoncrafter.de.hamster.flowchart.model.command;

import me.simoncrafter.de.hamster.flowchart.controller.FlowchartProgram;
import me.simoncrafter.de.hamster.flowchart.model.CommandObject;

/**
 * Dummy Objekt für die Toolbar
 * 
 * @author gerrit
 * 
 */
public class ToolbarCommandObject extends CommandObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7987744516360100294L;

	public ToolbarCommandObject(String command) {
		super(command);
		this.setType("command");
	}

	@Override
	public Object executeImpl(FlowchartProgram program) {
		return null;
	}

}

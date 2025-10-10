package me.simoncrafter.de.hamster.scratch.elements.booleans;

import java.util.ArrayList;

import me.simoncrafter.de.hamster.scratch.Renderable;
import me.simoncrafter.de.hamster.scratch.ScratchProgram;
import me.simoncrafter.de.hamster.scratch.elements.BooleanMethodObject;
import me.simoncrafter.de.hamster.scratch.elements.voids.FunctionResultException;
import me.simoncrafter.de.hamster.workbench.Utils;

public class FalseBooleanObject extends BooleanMethodObject {
	public FalseBooleanObject() {
		super("falsch", Utils.getFalse(), getParameter());
	}
	
	private static ArrayList<RType> getParameter() {
		ArrayList<RType> parameter = new ArrayList<RType>();
		return parameter;
	}

	@Override
	public Renderable clone() {
		FalseBooleanObject temp = new FalseBooleanObject();
		return temp;
	}

	@Override
	public Object performImplementation(ScratchProgram program) throws FunctionResultException {
		return false;
	}
	
	@Override
	public void writeSourceCode(StringBuffer buffer, int layer, boolean comment, boolean needsReturn) {
		startLine(buffer, layer, comment);
		buffer.append("false");
	}
}

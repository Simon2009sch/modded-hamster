package me.simoncrafter.de.hamster.scratch.elements.booleans;

import java.util.ArrayList;

import me.simoncrafter.de.hamster.scratch.Renderable;
import me.simoncrafter.de.hamster.scratch.ScratchProgram;
import me.simoncrafter.de.hamster.scratch.elements.BooleanMethodObject;
import me.simoncrafter.de.hamster.scratch.elements.voids.FunctionResultException;


public class MaulLeerBooleanObject extends BooleanMethodObject {
	public MaulLeerBooleanObject() {
		super("maulLeer", getParameter());
	}
	
	private static ArrayList<RType> getParameter() {
		ArrayList<RType> parameter = new ArrayList<RType>();
		return parameter;
	}

	@Override
	public Renderable clone() {
		MaulLeerBooleanObject temp = new MaulLeerBooleanObject();
		return temp;
	}

	@Override
	public Object performImplementation(ScratchProgram program) throws FunctionResultException {
		return hamster.maulLeer();
	}
}

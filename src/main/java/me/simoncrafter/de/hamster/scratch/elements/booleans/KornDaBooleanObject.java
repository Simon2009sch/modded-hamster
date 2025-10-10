package me.simoncrafter.de.hamster.scratch.elements.booleans;

import java.util.ArrayList;

import me.simoncrafter.de.hamster.scratch.Renderable;
import me.simoncrafter.de.hamster.scratch.ScratchProgram;
import me.simoncrafter.de.hamster.scratch.elements.BooleanMethodObject;
import me.simoncrafter.de.hamster.scratch.elements.voids.FunctionResultException;


public class KornDaBooleanObject extends BooleanMethodObject {
	public KornDaBooleanObject() {
		super("kornDa", getParameter());
	}
	
	private static ArrayList<RType> getParameter() {
		ArrayList<RType> parameter = new ArrayList<RType>();
		return parameter;
	}

	@Override
	public Renderable clone() {
		KornDaBooleanObject temp = new KornDaBooleanObject();
		return temp;
	}

	@Override
	public Object performImplementation(ScratchProgram program) throws FunctionResultException {
		return hamster.kornDa();
	}
}

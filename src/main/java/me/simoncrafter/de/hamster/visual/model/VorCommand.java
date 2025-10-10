package me.simoncrafter.de.hamster.visual.model;

import me.simoncrafter.de.hamster.interpreter.Hamster;

public class VorCommand extends HamsterCommand {

	@Override
	public Object perform() {
		Hamster p = new Hamster(hamster);
		p.linksUm();
		hamster.vor();
		System.out.println("vor();");
		return null;
	}
}

package me.simoncrafter.de.hamster.model;

import java.io.Serializable;

import me.simoncrafter.de.hamster.debugger.model.Hamster;
import me.simoncrafter.de.hamster.workbench.Utils;

/**
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class MaulLeerException extends HamsterException implements Serializable {
	public MaulLeerException(Hamster hamster) {
		super(hamster);
	}

	public String toString() {
		return Utils.getResource("hamster.MaulLeerException");
	}
}
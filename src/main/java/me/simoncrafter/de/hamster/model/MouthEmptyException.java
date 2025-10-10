package me.simoncrafter.de.hamster.model;

import java.io.Serializable;

import me.simoncrafter.de.hamster.debugger.model.Hamster;
import me.simoncrafter.de.hamster.workbench.Utils;

/**
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class MouthEmptyException extends MaulLeerException implements Serializable {
	public MouthEmptyException(Hamster hamster) {
		super(hamster);
	}
	
	public MouthEmptyException(MaulLeerException exc) {
		super(exc.hamster);
	}
}
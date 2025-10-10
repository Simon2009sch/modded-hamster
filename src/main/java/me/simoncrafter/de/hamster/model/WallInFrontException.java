package me.simoncrafter.de.hamster.model;

import java.io.Serializable;

import me.simoncrafter.de.hamster.debugger.model.Hamster;
import me.simoncrafter.de.hamster.workbench.Utils;

/**
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class WallInFrontException extends MauerDaException implements Serializable {

	public WallInFrontException(Hamster hamster, int reihe, int spalte) {
		super(hamster, reihe, spalte);
	}
	
	public WallInFrontException(MauerDaException e) {
		super(e.hamster, e.reihe, e.spalte);
	}


}
package me.simoncrafter.de.hamster.model;

import java.io.Serializable;

import me.simoncrafter.de.hamster.debugger.model.Hamster;
import me.simoncrafter.de.hamster.workbench.Utils;

/**
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class MauerDaException extends HamsterException implements Serializable {
	int reihe;
	int spalte;

	public MauerDaException(Hamster hamster, int reihe, int spalte) {
		super(hamster);
		this.reihe = reihe;
		this.spalte = spalte;
	}

	public int getReihe() {
		return reihe;
	}

	public int getSpalte() {
		return spalte;
	}

	public String toString() {
		return Utils.getResource("hamster.MauerDaException") + " (" + reihe + ", " + spalte + ")";
	}
}
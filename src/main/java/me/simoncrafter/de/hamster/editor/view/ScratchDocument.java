package me.simoncrafter.de.hamster.editor.view;

import me.simoncrafter.de.hamster.compiler.model.HamsterScratchLexer;
import me.simoncrafter.de.hamster.workbench.Workbench;

// Ruby
public class ScratchDocument extends JavaDocument {
	private static final long serialVersionUID = -2158878842179979282L;

	public ScratchDocument(boolean printing) {
		super(printing);
		// Scratch
		scanner = new HamsterScratchLexer();
		// addStyle("plain", getStyle("default"));
		addDocumentListener(this);
		initStyles(printing, Workbench.getWorkbench().getFontSize());
	}
}

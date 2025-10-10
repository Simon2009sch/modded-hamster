package me.simoncrafter.de.hamster.editor.view;

import me.simoncrafter.de.hamster.compiler.model.HamsterJavaScriptLexer;
import me.simoncrafter.de.hamster.workbench.Workbench;

// JavaScript
public class JavaScriptDocument extends JavaDocument {
	private static final long serialVersionUID = -2158878842179979272L;

	public JavaScriptDocument(boolean printing) {
		super(printing);
		// JavaScript
		scanner = new HamsterJavaScriptLexer();
		// addStyle("plain", getStyle("default"));
		addDocumentListener(this);
		initStyles(printing, Workbench.getWorkbench().getFontSize());
	}
}
package me.simoncrafter.de.hamster.editor.view;

import me.simoncrafter.de.hamster.mod.ColorManager;
import me.simoncrafter.de.hamster.mod.UIStyleController;

import java.awt.Color;

import javax.swing.JTextPane;

public class LineNumberPanel extends JTextPane {

	LineNumberDocument doc;

	public LineNumberPanel() {
		this.doc = new LineNumberDocument();
		this.setDocument(this.doc);
		this.setBackground(Color.LIGHT_GRAY);
		this.setEditable(false); // dibo 210110
		UIStyleController.putUIComponent("editor.texteditor.linenumbers", this);
	}

	public void setNumberOfLines(int noOfLines) {
		StringBuffer text = new StringBuffer();
		for (int i = 1; i <= noOfLines; i++) {
			text.append(i + "\n");
		}
		this.setText(text.toString());
	}
	
	// dibo 230309
	public void changeFontSize(int size) {
		doc.changeFontSize(size);
	}

}

class LineNumberDocument extends JavaDocument {

	public LineNumberDocument() {
		super(false);
	}
}

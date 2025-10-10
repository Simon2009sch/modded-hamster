package me.simoncrafter.de.hamster.editor.view;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import me.simoncrafter.de.hamster.model.HamsterFile;
import me.simoncrafter.de.hamster.workbench.Utils;

/**
 * @author Daniel
 */
public class FileTreeCellRenderer extends DefaultTreeCellRenderer {
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		if (leaf) {
			HamsterFile file = ((FileTreeNode) value).getHamsterFile();
			if (file.getType() == HamsterFile.TERRITORIUM) // dibo 260110
				setIcon(Utils.getIcon("resources/Terrain16.gif"));
			else if (file.getType() == HamsterFile.HAMSTERCLASS)
				setIcon(Utils.getIcon("resources/HamsterClass16.gif"));
			else if (file.getType() == HamsterFile.IMPERATIVE)
				setIcon(Utils.getIcon("resources/IHamster16.gif"));
			else if (file.getType() == HamsterFile.OBJECT)
				setIcon(Utils.getIcon("resources/OOHamster16.gif"));
			else if (file.getType() == HamsterFile.SCHEMEPROGRAM) // Martin
				setIcon(Utils.getIcon("resources/SchemeHamster16.gif"));
			else if (file.getType() == HamsterFile.PROLOGPROGRAM) // Prolog
				setIcon(Utils.getIcon("resources/PrologHamster16.gif"));
			else if (file.getType() == HamsterFile.PYTHONPROGRAM) // Python
				setIcon(Utils.getIcon("resources/PythonHamster16.gif"));
			else if (file.getType() == HamsterFile.JAVASCRIPTPROGRAM) // JavaScript
				setIcon(Utils.getIcon("resources/JavaScriptHamster16.gif"));
			else if (file.getType() == HamsterFile.RUBYPROGRAM) // Ruby
				setIcon(Utils.getIcon("resources/RubyHamster16.gif"));
			else if (file.getType() == HamsterFile.SCRATCHPROGRAM) // Scratch
				setIcon(Utils.getIcon("resources/ScratchHamster16.gif"));
			else if (file.getType() == HamsterFile.FSM) // FSM
				setIcon(Utils.getIcon("resources/FSMHamster16.gif"));
			else if (file.getType() == HamsterFile.FLOWCHART) // Flowchart
				setIcon(Utils.getIcon("resources/FlowchartHamster16.gif"));
			else if (file.getType() == HamsterFile.LEGOPROGRAM) // lego
				setIcon(Utils.getIcon("resources/Zahnrad16.gif"));
		}
		this.setBackgroundNonSelectionColor(tree.getBackground()); // dibo
																	// 230309
		// this.setBackgroundSelectionColor(new Color(240, 244, 246)); // dibo
		// 230309
		return this;
	}
}

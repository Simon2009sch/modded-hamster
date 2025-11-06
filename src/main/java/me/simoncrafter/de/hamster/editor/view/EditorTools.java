package me.simoncrafter.de.hamster.editor.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;

import me.simoncrafter.de.hamster.editor.controller.EditorController;
import me.simoncrafter.de.hamster.model.HamsterFile;
import me.simoncrafter.de.hamster.workbench.ForwardAction;
import me.simoncrafter.de.hamster.workbench.Utils;
import me.simoncrafter.de.hamster.workbench.Workbench;
import me.simoncrafter.de.hamster.workbench.WorkbenchView;

/**
 * 
 * @author $Author: djasper $
 * @version $Revision: 1.3 $
 */
public class EditorTools implements PropertyChangeListener {

	private HamsterFile activeFile;

	private SaveAction saveAction = new SaveAction();

	public class SaveAction extends ForwardAction {
		public SaveAction() {
			super("editor.save", EditorController.ACTION_SAVE);
		}
	}

	private SaveAsAction saveAsAction = new SaveAsAction();

	public class SaveAsAction extends ForwardAction {
		public SaveAsAction() {
			super("editor.saveas", EditorController.ACTION_SAVE_AS);
		}
	}

	private SavePlusAction savePlusAction = new SavePlusAction();

	public class SavePlusAction extends ForwardAction {
		public SavePlusAction() {
			super("editor.saveplus", EditorController.ACTION_SAVE_PLUS);
		}
	}

	private SaveAsPlusAction saveAsPlusAction = new SaveAsPlusAction();

	public class SaveAsPlusAction extends ForwardAction {
		public SaveAsPlusAction() {
			super("editor.saveasplus", EditorController.ACTION_SAVE_AS_PLUS);
		}
	}

	private GenerateAction generateAction = new GenerateAction();

	public class GenerateAction extends ForwardAction {
		public GenerateAction() {
			super("editor.generate", EditorController.ACTION_GENERATE);
		}
	}

	private CloseAction closeAction = new CloseAction();

	public class CloseAction extends ForwardAction {
		public CloseAction() {
			super("editor.close", EditorController.ACTION_CLOSE);
		}
	}

	private PrintAction printAction = new PrintAction();

	public class PrintAction extends ForwardAction {
		public PrintAction() {
			super("editor.print", EditorController.ACTION_PRINT);
		}
	}

	private NewAction newAction = new NewAction();

	public class NewAction extends ForwardAction {
		public NewAction() {
			super("editor.new", EditorController.ACTION_NEW);
		}
	}

	private OpenAction openAction = new OpenAction();

	public class OpenAction extends ForwardAction {
		public OpenAction() {
			super("editor.open", EditorController.ACTION_OPEN);
		}
	}

	private CutAction cutAction = new CutAction();

	public class CutAction extends DefaultEditorKit.CutAction {
		public CutAction() {
			Utils.setData(this, "editor.cut");
		}
	}

	private CopyAction copyAction = new CopyAction();

	public class CopyAction extends DefaultEditorKit.CopyAction {
		public CopyAction() {
			Utils.setData(this, "editor.copy");
			// dibo
			this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
					KeyEvent.VK_INSERT, InputEvent.CTRL_MASK));

		}
	}

	private PasteAction pasteAction = new PasteAction();

	public class PasteAction extends DefaultEditorKit.PasteAction {
		public PasteAction() {
			Utils.setData(this, "editor.paste");
			// dibo
			this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
					KeyEvent.VK_INSERT, InputEvent.SHIFT_MASK));
		}
	}

	private UndoAction undoAction = new UndoAction();

	public class UndoAction extends ForwardAction {
		public UndoAction() {
			super("editor.undo", EditorController.ACTION_UNDO);
		}
	}

	private RedoAction redoAction = new RedoAction();

	public class RedoAction extends ForwardAction {
		public RedoAction() {
			super("editor.redo", EditorController.ACTION_REDO);
		}
	}

	public EditorTools(EditorController controller) {
		WorkbenchView workbenchView = controller.getWorkbench().getView();

		// Create and add menu bar
		JMenu file = workbenchView.findMenu("editor", "file");
		file.add(new JMenuItem(newAction));
		file.add(new JMenuItem(openAction));
		file.addSeparator();
		file.add(new JMenuItem(saveAction));
		file.add(new JMenuItem(saveAsAction));
		file.add(new JMenuItem(savePlusAction));
		file.add(new JMenuItem(saveAsPlusAction));
		if (Utils.SCRATCH || Utils.FSM || Utils.FLOWCHART) {
			file.add(new JMenuItem(generateAction));
		}
		file.add(new JMenuItem(closeAction));
		file.addSeparator();
		file.add(new JMenuItem(printAction));
		file.addSeparator();

		JMenuItem exitItem = null;

		file.add(exitItem = new JMenuItem(Utils.getResource("editor.exit.text")));
		exitItem.setAccelerator(KeyStroke.getKeyStroke(
				Utils.getResource("editor.exit.mnemonic").charAt(0),
				InputEvent.CTRL_MASK));
		exitItem.addActionListener(new ExitAction());

		JMenu edit = workbenchView.findMenu("editor", "edit");
		edit.add(new JMenuItem(undoAction));
		edit.add(new JMenuItem(redoAction));
		edit.addSeparator();
		edit.add(new JMenuItem(cutAction));
		edit.add(new JMenuItem(copyAction));
		edit.add(new JMenuItem(pasteAction));

		// Create and add tool bar
		JToolBar toolbar = workbenchView.findToolBar("editor");
		toolbar.add(Utils.createStyledButton(newAction, "editor.toolbar.buttons.new"));
		toolbar.add(Box.createRigidArea(new Dimension(2, 2)));
		toolbar.add(Utils.createStyledButton(openAction, "editor.toolbar.buttons.open"));
		toolbar.add(Box.createRigidArea(new Dimension(2, 2)));
		toolbar.add(Utils.createStyledButton(saveAction, "editor.toolbar.buttons.save"));
		toolbar.add(Box.createRigidArea(new Dimension(2, 2)));
		toolbar.add(Utils.createStyledButton(saveAsAction, "editor.toolbar.buttons.saveas"));
		toolbar.add(Box.createRigidArea(new Dimension(2, 2)));
		toolbar.add(Utils.createStyledButton(savePlusAction, "editor.toolbar.buttons.saveplus"));
		toolbar.add(Box.createRigidArea(new Dimension(2, 2)));
		toolbar.add(Utils.createStyledButton(saveAsPlusAction, "editor.toolbar.buttons.saveasplus"));
		toolbar.add(Box.createRigidArea(new Dimension(2, 2)));
		toolbar.add(Utils.createStyledButton(closeAction, "editor.toolbar.buttons.close"));
		toolbar.add(Box.createRigidArea(new Dimension(2, 2)));
		toolbar.add(Utils.createStyledButton(printAction, "editor.toolbar.buttons.print"));

		toolbar.add(Box.createRigidArea(new Dimension(11, 11)));

		toolbar.add(Utils.createStyledButton(cutAction, "editor.toolbar.buttons.cut"));
		toolbar.add(Box.createRigidArea(new Dimension(2, 2)));
		toolbar.add(Utils.createStyledButton(copyAction, "editor.toolbar.buttons.copy"));
		toolbar.add(Box.createRigidArea(new Dimension(2, 2)));
		toolbar.add(Utils.createStyledButton(pasteAction, "editor.toolbar.buttons.paste"));

		toolbar.add(Box.createRigidArea(new Dimension(11, 11)));

		toolbar.add(Utils.createStyledButton(undoAction, "editor.toolbar.buttons.undo"));
		toolbar.add(Box.createRigidArea(new Dimension(2, 2)));
		toolbar.add(Utils.createStyledButton(redoAction, "editor.toolbar.buttons.redo"));

		saveAction.addActionListener(controller);
		saveAsAction.addActionListener(controller);
		savePlusAction.addActionListener(controller);
		saveAsPlusAction.addActionListener(controller);
		generateAction.addActionListener(controller);
		openAction.addActionListener(controller);
		newAction.addActionListener(controller);
		closeAction.addActionListener(controller);
		printAction.addActionListener(controller);
		undoAction.addActionListener(controller);
		redoAction.addActionListener(controller);

		updateButtonStates();
	}

	class ExitAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			Workbench.getWorkbench().close(null);
		}

	}

	public void updateButtonStates() {
		printAction.setEnabled(activeFile != null);
		closeAction.setEnabled(activeFile != null);
		if (activeFile == null) {
			saveAction.setEnabled(false);
			saveAsAction.setEnabled(false);
			savePlusAction.setEnabled(false);
			saveAsPlusAction.setEnabled(false);
			generateAction.setEnabled(false);
		} else {
			if (!activeFile.exists() || activeFile.isModified()) {
				saveAction.setEnabled(true);
			} else {
				saveAction.setEnabled(false);
			}
			saveAsAction.setEnabled(true);
			savePlusAction.setEnabled(true);
			saveAsPlusAction.setEnabled(true);
			generateAction
					.setEnabled(!activeFile.isDirectory()
							&& (activeFile.getType() == HamsterFile.SCRATCHPROGRAM
									|| activeFile.getType() == HamsterFile.FSM || activeFile
									.getType() == HamsterFile.FLOWCHART));
		}
	}

	public void setActiveFile(HamsterFile file) {
		if (activeFile != null) {
			activeFile.removePropertyChangeListener(this);
		}
		activeFile = file;
		if (file != null)
			file.addPropertyChangeListener(this);
		updateButtonStates();
	}

	public void updateButtons(TextArea textArea) {
		if (textArea == null) {
			undoAction.setEnabled(false);
			redoAction.setEnabled(false);
		} else {
			undoAction.setEnabled(textArea.getUndoManager().canUndo());
			redoAction.setEnabled(textArea.getUndoManager().canRedo());
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		updateButtonStates();
	}
}

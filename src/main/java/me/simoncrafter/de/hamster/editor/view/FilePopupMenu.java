package me.simoncrafter.de.hamster.editor.view;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import me.simoncrafter.de.hamster.editor.controller.EditorController;
import me.simoncrafter.de.hamster.model.HamsterFile;
import me.simoncrafter.de.hamster.workbench.ForwardAction;
import me.simoncrafter.de.hamster.workbench.Utils;

/**
 * @author $Author: djasper $
 * @version $Revision: 1.2 $
 */
public class FilePopupMenu extends JPopupMenu {
	protected HamsterFile file;

	public class OpenAction extends ForwardAction {
		public OpenAction(ActionListener forwardTo) {
			super("editor.popup.file.open", EditorController.FILE_OPEN);
			addActionListener(forwardTo);
		}
	}

	public class NewFolderAction extends ForwardAction {
		public NewFolderAction(ActionListener forwardTo) {
			super("editor.popup.file.newfolder",
					EditorController.FILE_NEWFOLDER);
			addActionListener(forwardTo);
		}
	}

	public class DeleteAction extends ForwardAction {
		public DeleteAction(ActionListener forwardTo) {
			super("editor.popup.file.delete", EditorController.FILE_DELETE);
			addActionListener(forwardTo);
		}
	}

	public class CopyAction extends ForwardAction {
		public CopyAction(ActionListener forwardTo) {
			super("editor.popup.file.copy", EditorController.FILE_COPY);
			addActionListener(forwardTo);
		}
	}

	public class PasteAction extends ForwardAction {
		public PasteAction(ActionListener forwardTo) {
			super("editor.popup.file.paste", EditorController.FILE_PASTE);
			addActionListener(forwardTo);
		}
	}

	public class RenameAction extends ForwardAction {
		public RenameAction(ActionListener forwardTo) {
			super("editor.popup.file.rename", EditorController.FILE_RENAME);
			addActionListener(forwardTo);
		}
	}

	public class GenerateAction extends ForwardAction {
		public GenerateAction(ActionListener forwardTo) {
			super("editor.popup.file.generate", EditorController.FILE_GENERATE);
			addActionListener(forwardTo);
		}
	}

	protected OpenAction openAction;
	protected NewFolderAction newFolderAction;
	protected DeleteAction deleteAction;
	protected CopyAction copyAction;
	protected PasteAction pasteAction;
	protected RenameAction renameAction;
	protected GenerateAction generateAction;

	public FilePopupMenu(EditorController controller) {
		openAction = new OpenAction(controller);
		add(createMenuItem(openAction));
		newFolderAction = new NewFolderAction(controller);
		add(createMenuItem(newFolderAction));
		deleteAction = new DeleteAction(controller);
		add(createMenuItem(deleteAction));
		copyAction = new CopyAction(controller);
		add(createMenuItem(copyAction));
		pasteAction = new PasteAction(controller);
		add(createMenuItem(pasteAction));
		renameAction = new RenameAction(controller);
		add(createMenuItem(renameAction));
		generateAction = new GenerateAction(controller);
		if (Utils.SCRATCH || Utils.FSM || Utils.FLOWCHART) {
			add(createMenuItem(generateAction));
		}// test
	}

	private JMenuItem createMenuItem(Action action) {
		JMenuItem item = new JMenuItem(action);
		item.setBackground(Color.RED);
		item.setBorder(BorderFactory.createEmptyBorder());
		return item;
	}


	public void setFile(HamsterFile file) {
		this.file = file;
		openAction.setEnabled(!file.isDirectory());
		newFolderAction.setEnabled(file.isDirectory());
		copyAction.setEnabled(!file.isDirectory());
		pasteAction.setEnabled(file.isDirectory());
		deleteAction.setEnabled(file != HamsterFile.getHamsterFile(Utils.HOME));
		renameAction.setEnabled(file != HamsterFile.getHamsterFile(Utils.HOME));
		generateAction
				.setEnabled(!file.isDirectory()
						&& (file.getType() == HamsterFile.SCRATCHPROGRAM
								|| file.getType() == HamsterFile.FSM || file
								.getType() == HamsterFile.FLOWCHART));
	}

	public HamsterFile getFile() {
		return file;
	}
}
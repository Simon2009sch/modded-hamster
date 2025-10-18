package me.simoncrafter.de.hamster.debugger.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import me.simoncrafter.de.hamster.debugger.controller.DebuggerController;
import me.simoncrafter.de.hamster.debugger.model.DebuggerModel;
import me.simoncrafter.de.hamster.styles.controller.UIStyleController;
import me.simoncrafter.de.hamster.model.HamsterFile;
import me.simoncrafter.de.hamster.workbench.ForwardAction;
import me.simoncrafter.de.hamster.workbench.Utils;

/**
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class DebuggerTools implements Observer, ChangeListener {
	private DebuggerModel model;

	private StartAction startAction = new StartAction();

	public class StartAction extends ForwardAction {
		public StartAction() {
			super("debugger.start", DebuggerController.ACTION_START);
		}
	}

	private StepAction stepAction = new StepAction();

	public class StepAction extends ForwardAction {
		public StepAction() {
			super("debugger.stepinto", DebuggerController.ACTION_STEP);
		}
	}

	private StepOverAction stepOverAction = new StepOverAction();

	public class StepOverAction extends ForwardAction {
		public StepOverAction() {
			super("debugger.stepover", DebuggerController.ACTION_STEPOVER);
		}
	}

	private EnableAction enableAction = new EnableAction();

	public class EnableAction extends ForwardAction {
		public EnableAction() {
			super("debugger.enable", DebuggerController.ACTION_ENABLE);
		}
	}

	private StopAction stopAction = new StopAction();

	public class StopAction extends ForwardAction {
		public StopAction() {
			super("debugger.stop", DebuggerController.ACTION_STOP);
		}
	}

	private PauseAction pauseAction = new PauseAction();

	public class PauseAction extends ForwardAction {
		public PauseAction() {
			super("debugger.pause", DebuggerController.ACTION_PAUSE);
		}
	}

	private JToggleButton enableButton;

	private JCheckBoxMenuItem enableItem;

	private JSlider delay, delaySim, delay3D;

	protected HamsterFile activeFile;

	public DebuggerTools(DebuggerModel model, final DebuggerController controller) {
		this.model = model;
		model.addObserver(this);

		JMenu menu = controller.getWorkbench().getView().findMenu("editor", "debugger");
		menu.add(new JMenuItem(startAction));
		menu.add(new JMenuItem(pauseAction));
		menu.add(new JMenuItem(stopAction));
		menu.addSeparator();
		enableItem = new JCheckBoxMenuItem(enableAction);
		menu.add(enableItem);
		menu.add(new JMenuItem(stepAction));
		menu.add(new JMenuItem(stepOverAction));

		JToolBar toolBar = controller.getWorkbench().getView().findToolBar("editor");
		toolBar.add(Box.createRigidArea(new Dimension(11, 11)));
		toolBar.add(Utils.createStyledButton(startAction, "editor.debugger.toolbar.buttons.start"));
		toolBar.add(Box.createRigidArea(new Dimension(2, 2)));
		toolBar.add(Utils.createStyledButton(pauseAction, "editor.debugger.toolbar.buttons.pause"));
		toolBar.add(Box.createRigidArea(new Dimension(2, 2)));
		toolBar.add(Utils.createStyledButton(stopAction, "editor.debugger.toolbar.buttons.stop"));
		toolBar.add(Box.createRigidArea(new Dimension(2, 2)));
		enableButton = Utils.createStyledToggleButton(enableAction, "editor.debugger.toolbar.buttons.enable");
		toolBar.add(enableButton);
		toolBar.add(Box.createRigidArea(new Dimension(2, 2)));
		toolBar.add(Utils.createStyledButton(stepAction, "editor.debugger.toolbar.buttons.step"));
		toolBar.add(Box.createRigidArea(new Dimension(2, 2)));
		toolBar.add(Utils.createStyledButton(stepOverAction, "editor.debugger.toolbar.buttons.stepover"));
		toolBar.add(Box.createRigidArea(new Dimension(2, 2)));
		delay = new JSlider(0, 1000, 500);
		delay.setToolTipText(Utils.getResource("debugger.delay.tooltip"));
		delay.addChangeListener(controller);
		delay.setInverted(true);

		// delay.setSnapToTicks(true);
		delay.setMajorTickSpacing(100);
		delay.setPaintTicks(true);
		delay.addChangeListener(this);
		delay.setName("delay");
		delay.setBackground(toolBar.getBackground());
		toolBar.add(delay);

		UIStyleController.putUIComponent("editor.debugger.toolbar.delay", delay);

		JToolBar simulationBar = controller.getWorkbench().getView().findToolBar("simulation");

		simulationBar.add(Box.createRigidArea(new Dimension(11, 11)));
		JButton button = Utils.createStyledButton(startAction, "simulation.debugger.toolbar.buttons.start");

		KeyStroke keyM = KeyStroke.getKeyStroke(Utils.getResource("debugger.start.keystroke"));
		Action actionM = new AbstractAction("start2") {
			public void actionPerformed(ActionEvent e) {
				ActionEvent event = new ActionEvent(e.getSource(), e.getID(), DebuggerController.ACTION_START);
				controller.actionPerformed(event);
			}
		};
		button.getActionMap().put("start2", actionM);
		button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyM, "start2");

		simulationBar.add(button);

		simulationBar.add(Box.createRigidArea(new Dimension(2, 2)));

		button = Utils.createStyledButton(pauseAction, "simulation.debugger.toolbar.buttons.pause");

		keyM = KeyStroke.getKeyStroke(Utils.getResource("debugger.pause.keystroke"));
		actionM = new AbstractAction("start2") {
			public void actionPerformed(ActionEvent e) {
				ActionEvent event = new ActionEvent(e.getSource(), e.getID(), DebuggerController.ACTION_PAUSE);
				controller.actionPerformed(event);
			}
		};
		button.getActionMap().put("start2", actionM);
		button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyM, "start2");
		simulationBar.add(button);

		simulationBar.add(Box.createRigidArea(new Dimension(2, 2)));

		button = Utils.createStyledButton(stopAction, "simulation.debugger.toolbar.buttons.stop");

		keyM = KeyStroke.getKeyStroke(Utils.getResource("debugger.stop.keystroke"));
		actionM = new AbstractAction("start2") {
			public void actionPerformed(ActionEvent e) {
				ActionEvent event = new ActionEvent(e.getSource(), e.getID(), DebuggerController.ACTION_STOP);
				controller.actionPerformed(event);
			}
		};
		button.getActionMap().put("start2", actionM);
		button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyM, "start2");
		simulationBar.add(button);

		delaySim = new JSlider(0, 1000, 500);
		delaySim.setToolTipText(Utils.getResource("debugger.delay.tooltip"));
		delaySim.addChangeListener(controller);
		delaySim.setInverted(true);
		// delaySim.setSnapToTicks(true);
		delaySim.setMajorTickSpacing(100);
		delaySim.setPaintTicks(true);
		delaySim.setName("sim");
		delaySim.setBackground(simulationBar.getBackground());
		delaySim.addChangeListener(this);
		simulationBar.add(Box.createRigidArea(new Dimension(2, 2)));
		simulationBar.add(delaySim);

		UIStyleController.putUIComponent("simulation.debugger.toolbar.delay", delaySim);

		startAction.addActionListener(controller);
		stepAction.addActionListener(controller);
		stepOverAction.addActionListener(controller);
		stopAction.addActionListener(controller);
		enableAction.addActionListener(controller);
		pauseAction.addActionListener(controller);

		// chris
		JToolBar simulationBar3D = controller.getWorkbench().getView().findToolBar("3dsimulation");
		simulationBar3D.add(Box.createRigidArea(new Dimension(11, 11)));
		simulationBar3D.add(Utils.createStyledButton(startAction, "3dsimulation.debugger.toolbar.buttons.start"));
		simulationBar3D.add(Box.createRigidArea(new Dimension(2, 2)));
		simulationBar3D.add(Utils.createStyledButton(pauseAction, "3dsimulation.debugger.toolbar.buttons.pause"));
		simulationBar3D.add(Box.createRigidArea(new Dimension(2, 2)));
		simulationBar3D.add(Utils.createStyledButton(stopAction, "3dsimulation.debugger.toolbar.buttons.stop"));

		simulationBar3D.add(Box.createRigidArea(new Dimension(2, 2)));
		delay3D = new JSlider(0, 1000, 500);
		delay3D.setToolTipText(Utils.getResource("debugger.delay.tooltip"));
		delay3D.addChangeListener(controller);
		delay3D.setInverted(true);
		// delay3D.setSnapToTicks(true);
		delay3D.setMajorTickSpacing(100);
		delay3D.setPaintTicks(true);
		delay3D.setName("3d");
		delay3D.addChangeListener(this);
		simulationBar3D.add(Box.createRigidArea(new Dimension(2, 2)));
		simulationBar3D.add(delay3D);

		UIStyleController.putUIComponent("3dsimulation.debugger.toolbar.delay", delay3D);

		updateButtonStates();
	}

	public void update(Observable o, Object arg) {
		updateButtonStates();
		if (arg == DebuggerModel.ARG_ENABLE) {
			enableButton.setSelected(model.isEnabled());
			enableItem.setSelected(model.isEnabled());
		}
	}

	public void updateButtonStates() {

		// martin
		// Falls die aktive Datei ein Scheme-Programm ist, muessen
		// ein paar Buttons ausgeblendet werden.
		// Martin + Python + Ruby + JavaScript
		if (activeFile != null && (activeFile.getType() == HamsterFile.SCHEMEPROGRAM
				|| activeFile.getType() == HamsterFile.PYTHONPROGRAM || activeFile.getType() == HamsterFile.RUBYPROGRAM
				|| activeFile.getType() == HamsterFile.JAVASCRIPTPROGRAM)) {
			enableAction.setEnabled(false);
			stopAction.setEnabled(model.getState() != DebuggerModel.NOT_RUNNING);
			pauseAction.setEnabled(false);
			stepAction.setEnabled(false);
			stepOverAction.setEnabled(false);
			startAction.setEnabled(activeFile != null
					&& (model.getState() != DebuggerModel.RUNNING && !(activeFile.getType() == HamsterFile.HAMSTERCLASS
							&& model.getState() == DebuggerModel.NOT_RUNNING)));
			/*
			 * diboxy if (me.simoncrafter.de.hamster.workbench.Workbench.workbench != null) {
			 * me.simoncrafter.de.hamster.workbench.Workbench.getWorkbench().getSimulation()
			 * .getSimulationTools().getResetAction() .setEnabled(false); }
			 */
			// Reset-Button im Sim-Fenster.
			me.simoncrafter.de.hamster.workbench.Workbench.getWorkbench().getSimulation().getSimulationTools().getResetAction()
					.setEnabled(me.simoncrafter.de.hamster.workbench.Workbench.getWorkbench().getSimulation()
							.getSimulationModel().savedTerrain != null);
			// Prolog
		} else if (activeFile != null && activeFile.getType() == HamsterFile.PROLOGPROGRAM) {
			// Spezielle Debugger-Buttons deaktiviert..
			enableAction.setEnabled(false);
			stepAction.setEnabled(false);
			stepOverAction.setEnabled(false);

			// Stop-Button
			stopAction.setEnabled(true);

			pauseAction.setEnabled(false);

			startAction.setEnabled(activeFile != null
					&& (model.getState() != DebuggerModel.RUNNING && !(activeFile.getType() == HamsterFile.HAMSTERCLASS
							&& model.getState() == DebuggerModel.NOT_RUNNING)));
			// Reset-Button im Sim-Fenster.
			me.simoncrafter.de.hamster.workbench.Workbench.getWorkbench().getSimulation().getSimulationTools().getResetAction()
					.setEnabled(me.simoncrafter.de.hamster.workbench.Workbench.getWorkbench().getSimulation()
							.getSimulationModel().savedTerrain != null
							&& (model.getState() != DebuggerModel.RUNNING && model.getState() != DebuggerModel.PAUSED));
		} else {
			startAction.setEnabled(activeFile != null
					&& (model.getState() != DebuggerModel.RUNNING && !(activeFile.getType() == HamsterFile.HAMSTERCLASS
							&& model.getState() == DebuggerModel.NOT_RUNNING)));
			pauseAction.setEnabled(model.getState() == DebuggerModel.RUNNING);
			if (activeFile != null && (activeFile.getType() == HamsterFile.SCRATCHPROGRAM
					|| activeFile.getType() == HamsterFile.FSM || activeFile.getType() == HamsterFile.FLOWCHART)) {
				if (model.getState() != DebuggerModel.RUNNING) { // dibo 300710
					stepAction.setEnabled(true);
				} else {
					stepAction.setEnabled(false);
				}
				stepOverAction.setEnabled(false); // dibo 290710
				enableAction.setEnabled(false);
			} else {
				stepAction.setEnabled(model.isEnabled() && (model.getState() == DebuggerModel.PAUSED
						|| (model.getState() == DebuggerModel.NOT_RUNNING) && startAction.isEnabled()));
				stepOverAction.setEnabled(model.isEnabled() && model.getState() == DebuggerModel.PAUSED);
				enableAction.setEnabled(!Utils.runlocally);
			}
			stopAction.setEnabled(model.getState() != DebuggerModel.NOT_RUNNING);

			if (me.simoncrafter.de.hamster.workbench.Workbench.workbench != null) {
				me.simoncrafter.de.hamster.workbench.Workbench.getWorkbench().getSimulation().getSimulationTools().getResetAction()
						.setEnabled(me.simoncrafter.de.hamster.workbench.Workbench.getWorkbench().getSimulation()
								.getSimulationModel().savedTerrain != null
								&& activeFile != null
								&& (model.getState() != DebuggerModel.RUNNING
										&& model.getState() != DebuggerModel.PAUSED
										&& !(activeFile.getType() == HamsterFile.HAMSTERCLASS
												&& model.getState() == DebuggerModel.NOT_RUNNING)));
			}
		}

	}

	public void setActiveFile(HamsterFile activeFile) {
		this.activeFile = activeFile;
		updateButtonStates();
	}

	public void stateChanged(ChangeEvent e) {
		JSlider slider = (JSlider) e.getSource();
		if (slider == delay) {
			if (delaySim.getValue() != delay.getValue()) {
				delaySim.setValue(delay.getValue());
			}
			if (delay3D.getValue() != delay.getValue()) { // chris
				delay3D.setValue(delay.getValue());
			}
		} else if (slider == delaySim) {
			if (delay.getValue() != delaySim.getValue()) {
				delay.setValue(delaySim.getValue());
			}
			if (delay3D.getValue() != delaySim.getValue()) { // chris
				delay3D.setValue(delaySim.getValue());
			}
		} else if (slider == delay3D) { // chris
			if (delay.getValue() != delay3D.getValue()) {
				delay.setValue(delay3D.getValue());
			}
			if (delaySim.getValue() != delay3D.getValue()) {
				delaySim.setValue(delay3D.getValue());
			}
		}

	}
}

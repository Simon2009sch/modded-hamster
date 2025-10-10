package me.simoncrafter.de.hamster.fsm.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import me.simoncrafter.de.hamster.editor.view.TextArea;
import me.simoncrafter.de.hamster.fsm.FsmUtils;
import me.simoncrafter.de.hamster.fsm.controller.FsmHamsterFile;
import me.simoncrafter.de.hamster.fsm.controller.FsmProgram;
import me.simoncrafter.de.hamster.fsm.controller.handler.CreateCommentHandler;
import me.simoncrafter.de.hamster.fsm.controller.handler.CreateStateHandler;
import me.simoncrafter.de.hamster.fsm.controller.handler.CreateTransistionHandler;
import me.simoncrafter.de.hamster.fsm.controller.handler.DeleteModeHandler;
import me.simoncrafter.de.hamster.fsm.controller.handler.EditModeHandler;
import me.simoncrafter.de.hamster.fsm.controller.handler.LayoutHandler;
import me.simoncrafter.de.hamster.fsm.controller.handler.MarkFinalStateHandler;
import me.simoncrafter.de.hamster.fsm.controller.handler.MarkStartStateHandler;
import me.simoncrafter.de.hamster.fsm.controller.handler.TypeOfFsmHandler;
import me.simoncrafter.de.hamster.fsm.controller.handler.ZoomInHandler;
import me.simoncrafter.de.hamster.fsm.controller.handler.ZoomOutHandler;
import me.simoncrafter.de.hamster.fsm.model.CommentObject;
import me.simoncrafter.de.hamster.fsm.model.FsmObject;
import me.simoncrafter.de.hamster.fsm.model.RenderableObject;
import me.simoncrafter.de.hamster.fsm.model.state.StateObject;
import me.simoncrafter.de.hamster.fsm.model.transition.BooleanObject;
import me.simoncrafter.de.hamster.fsm.model.transition.InputObject;
import me.simoncrafter.de.hamster.fsm.model.transition.TransitionDescriptionObject;
import me.simoncrafter.de.hamster.fsm.model.transition.TransitionObject;
import me.simoncrafter.de.hamster.fsm.model.transition.VoidObject;
import me.simoncrafter.de.hamster.fsm.model.transition.input.EpsilonBooleanObject;
import me.simoncrafter.de.hamster.fsm.model.transition.output.EpsilonFunctionObject;
import me.simoncrafter.de.hamster.workbench.Utils;

/**
 * Diese Klasse repräsentiert die Zeichenfläche für ein FSM-Programm in der GUI
 * des Hamster-Simulators
 * 
 * @author dibo, raffaela ferrari
 * 
 */
public class FsmPanel extends JPanel implements ContextMenuPanel{
	private static ImageIcon editImg;
	private static ImageIcon deleteImg;
	private static ImageIcon createStateImg;
	private static ImageIcon markStartStateImg;
	private static ImageIcon markFinalStateImg;
	private static ImageIcon createTransistionImg;
	private static ImageIcon createCommentImg;
	private static ImageIcon zoomInImg;
	private static ImageIcon zoomOutImg;
	private static ImageIcon layoutImg;

	// für interne Zwecke
	private TextArea textArea;

	// das zugeordnete FSM-HamsterFile, über das das aktuelle FSM-Programm
	// ermittelt werden kann
	private FsmHamsterFile file;
	
	//GUI Elemente
	private FsmAutomataPanel automataPanel;
	private JScrollPane scrollPane;
	
	private JToggleButton editMode;
	private JToggleButton deleteMode;
	private JToggleButton createState;
	private JToggleButton markStartState;
	private JToggleButton markFinalState;
	private JToggleButton createTransistion;
	private JToggleButton createComment;
	
	private JButton zoomIn;
	private JButton zoomOut;
	private JButton layout;
	private JRadioButton deterministic;
	private JRadioButton nondeterministic;

	/**
	 * Erzeugt das FSM-Panel für das übergebene File (inkl. Programm)
	 * 
	 * @param file
	 */
	public FsmPanel(FsmHamsterFile file) {
		this.file = file; // wichtig: nicht ändern!
		this.automataPanel = new FsmAutomataPanel(this);
		
		this.loadImages();
		this.setLayout(new BorderLayout());
		this.add(createMenu(),BorderLayout.PAGE_START);
		this.add(createFSMMenu(), BorderLayout.LINE_START);
		
		scrollPane = new JScrollPane(this.automataPanel);
		scrollPane.setPreferredSize(new Dimension(400,400));
		this.add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * Erstellt das Menü auf der linken Seite
	 */
	private JToolBar createFSMMenu() {
		JToolBar toolbar = new JToolBar();
		toolbar.setOrientation(SwingConstants.VERTICAL);
		toolbar.setFloatable(false);
		toolbar.setMargin(new Insets(5,-3, 0, 2));
		toolbar.setBackground(new Color(219,255,179));
		
		editMode = FsmUtils.createToggleButton(editImg);
		editMode.setToolTipText("Editieren");
		editMode.addMouseListener(new EditModeHandler(this.automataPanel));

		deleteMode = FsmUtils.createToggleButton(deleteImg);
		deleteMode.setToolTipText("L�schen");
		deleteMode.addMouseListener(new DeleteModeHandler(this.automataPanel));
		
		createState = FsmUtils.createToggleButton(createStateImg);
		createState.setToolTipText("Zustand erzeugen");
		createState.addMouseListener(new CreateStateHandler(this.automataPanel));
		
		markStartState = FsmUtils.createToggleButton(markStartStateImg);
		markStartState.setToolTipText("Startzustand markieren");
		markStartState.addMouseListener(new MarkStartStateHandler(this.automataPanel));
		
		markFinalState = FsmUtils.createToggleButton(markFinalStateImg);
		markFinalState.setToolTipText("Endzustand markieren");
		markFinalState.addMouseListener(new MarkFinalStateHandler(this.automataPanel));
		
		createTransistion = FsmUtils.createToggleButton(createTransistionImg);
		createTransistion.setToolTipText("Transition erzeugen");
		createTransistion.addMouseListener(new CreateTransistionHandler(this.automataPanel));
		
		createComment = FsmUtils.createToggleButton(createCommentImg);
		createComment.setToolTipText("Kommentar erzeugen");
		createComment.addMouseListener(new CreateCommentHandler(this.automataPanel));
		
		//Nur ein Button darf aktiv sein
		ButtonGroup group=new ButtonGroup();
		group.add(editMode);
		group.add(deleteMode);
		group.add(createState);
		group.add(markStartState);
		group.add(markFinalState);
		group.add(createTransistion);
		group.add(createComment);
		group.setSelected(editMode.getModel(), true);
		
		toolbar.add(editMode);
		toolbar.add(Box.createVerticalStrut(2));
		toolbar.add(deleteMode);

		toolbar.addSeparator();
		toolbar.add(createState);
		toolbar.add(Box.createVerticalStrut(2));
		toolbar.add(markStartState);
		toolbar.add(Box.createVerticalStrut(2));
		toolbar.add(markFinalState);
		toolbar.add(Box.createVerticalStrut(2));
		toolbar.add(createTransistion);
		toolbar.add(Box.createVerticalStrut(2));
		toolbar.add(createComment);
		
		return toolbar;
	}

	/**
	 * kreiert das Menü oben
	 */
	private JToolBar createMenu() {
		JToolBar toolbar = new JToolBar();
		toolbar.setOrientation(SwingConstants.HORIZONTAL);
		toolbar.setFloatable(false);
		toolbar.setMargin(new Insets(2, 35, 2, 0));
		toolbar.setBackground(new Color(219,255,179));

		zoomIn = FsmUtils.createButton(zoomInImg);
		zoomIn.setToolTipText("Vergr��ern");
		zoomIn.addMouseListener(new ZoomInHandler(this));

		zoomOut = FsmUtils.createButton(zoomOutImg);
		zoomOut.setToolTipText("Verkleinern");
		zoomOut.addMouseListener(new ZoomOutHandler(this));

		layout = FsmUtils.createButton(layoutImg);
		layout.setToolTipText("Automatisch layouten");
		layout.addMouseListener(new LayoutHandler(this));
		
		deterministic = new JRadioButton("deterministisch");
		deterministic.setToolTipText("Der endliche Automat wird deterministisch");
		deterministic.setBackground(new Color(219,255,179));
		deterministic.addMouseListener(new TypeOfFsmHandler(this, false));
		
		nondeterministic = new JRadioButton("nichtdeterministisch");
		nondeterministic.setToolTipText("Der endliche Automat wird nichtdeterministisch");
		nondeterministic.setBackground(new Color(219,255,179));
		nondeterministic.addMouseListener(new TypeOfFsmHandler(this, true));
		
		ButtonGroup group = new ButtonGroup();
	    group.add(deterministic);
	    group.add(nondeterministic);
	    if(this.getFsmProgram().isNondeterministic()) {
	    	group.setSelected(nondeterministic.getModel(), true);
	    } else {
	    	group.setSelected(deterministic.getModel(), true);
	    }
	    
		
		toolbar.add(zoomIn);
		toolbar.add(Box.createHorizontalStrut(2));
		toolbar.add(zoomOut);
		toolbar.add(Box.createHorizontalStrut(2));
		toolbar.add(Box.createHorizontalStrut(2));
		toolbar.add(layout);
		toolbar.addSeparator();
		toolbar.add(deterministic);
		toolbar.add(nondeterministic);
		
		return toolbar;
	}

	/**
	 * lädt alle statischen Bilder, die für das Menü benötigt werden.
	 * Falls eines schon geladen ist, so müssen die anderen nicht mehr
	 * geladen werden.
	 */
	private void loadImages() {
		if(editImg !=null) {
			return;
		}
		editImg = Utils.getIcon("resources/fsm/EditMode24.gif");
		deleteImg = Utils.getIcon("resources/Delete24.gif");
		createStateImg = Utils.getIcon("resources/fsm/CreateState24.gif");
		markStartStateImg = Utils.getIcon("resources/fsm/StartState24.gif");
		markFinalStateImg = Utils.getIcon("resources/fsm/FinalState24.gif");
		createTransistionImg = Utils.getIcon("resources/fsm/CreateTransition24.gif");
		createCommentImg = Utils.getIcon("resources/fsm/CreateComment24.gif");
		zoomInImg = Utils.getIcon("resources/ZoomIn24.gif");
		zoomOutImg = Utils.getIcon("resources/ZoomOut24.gif");
		layoutImg = Utils.getIcon("resources/fsm/Layout24.gif");
	}

	/**
	 * Setzt die zugeordnete TextArea (nicht verändern)
	 */
	public void setTextArea(TextArea area) {
		this.textArea = area;
		this.file.getProgram().setUpdateHandler(this.automataPanel);
	}

	/**
	 * Liefert die zugeordnete TextArea (nicht verändern)
	 */
	public TextArea getTextArea() {
		return this.textArea;
	}

	/**
	 * Liefert das zugehörige FsmAutomataPanel
	 */
	public FsmAutomataPanel getAutomataPanel() {
		return automataPanel;
	}

	/**
	 * Liefert das gerade aktuelle FsmProgramm.
	 * @return Das aktuelle FsmProgramm.
	 */
	public FsmProgram getFsmProgram() {
		return this.file.getProgram();
	}

	/**
	 * Setzt fest, dass das Panel modifiziert wurde, so dass der Speicherbutton
	 * enabled wird (nicht verändern)
	 */
	public void modified() {
		if (this.textArea == null) {
			return;
		}
		this.textArea.getFile().setModified(true);
	}

	/**
	 * Setzt fest, dass das Panel nicht modifiziert wurde, so dass der
	 * Speicherbutton disabled wird (nicht verändern)
	 */
	public void unmodified() {
		if (this.textArea == null) {
			return;
		}
		this.textArea.getFile().setModified(false);
	}

	/**
	 * Wird aufgerufen, wenn ein FSM-Programm gestartet bzw. gestoppt wird.
	 * Während einer Programm-Ausführung sollte das Programm nicht verändert
	 * werden können (locked == true)
	 */
	public void setLocked(boolean locked) {
		if(locked) {
			this.setEnabled(false);
			this.automataPanel.setLocked(true);
			editMode.setEnabled(false);
			deleteMode.setEnabled(false);
			createState.setEnabled(false);
			markStartState.setEnabled(false);
			markFinalState.setEnabled(false);
			createTransistion.setEnabled(false);
			createComment.setEnabled(false);
			zoomIn.setEnabled(false);
			zoomOut.setEnabled(false);
			layout.setEnabled(false);
			nondeterministic.setEnabled(false);
			deterministic.setEnabled(false);
		} else {
			this.setEnabled(true);
			this.automataPanel.setLocked(false);
			editMode.setEnabled(true);
			deleteMode.setEnabled(true);
			createState.setEnabled(true);
			markStartState.setEnabled(true);
			markFinalState.setEnabled(true);
			createTransistion.setEnabled(true);
			createComment.setEnabled(true);
			zoomIn.setEnabled(true);
			zoomOut.setEnabled(true);
			layout.setEnabled(true);
			nondeterministic.setEnabled(true);
			deterministic.setEnabled(true);
		}
	}

	/**
	 *  Für das Drucken; liefert die einzelnen Druckseiten
	 *  @return Einzelne Druckseiten
	 */
	public BufferedImage[] getImages() {
		BufferedImage[] images = new BufferedImage[1];
		images[0] = this.automataPanel.getImage();
		return images;
	}
	
	/**
	 * Erstellt einen neuen Zustand an der gegebenen Position.
	 * @param x x-Koordinate des Zustands
	 * @param y y-Koordinate des Zustands
	 */
	public void createState(int x, int y) {
		StateObject newState = new StateObject(false, false);
		newState.setCoordinates(x,y);
		getFsmProgram().addState(newState);
		this.modified();
		this.automataPanel.refresh();
	}
	
	/**
	 * Erstellt einen neuen Kommentar an der gegebenen Position.
	 * @param text der Text des Kommentars
	 * @param x x-Koordinate des Kommentars
	 * @param y y-Koordinate des Kommentars
	 */
	public void createComment(String text, int x, int y) {
		CommentObject newComment = new CommentObject();
		newComment.setName(text);
		newComment.setCoordinates(x,y);
		getFsmProgram().addComment(newComment);
		this.modified();
		this.automataPanel.refresh();
		this.automataPanel.setLocked(false);
	}
	
	/**
	 * Fügt eine neue Transition hinzu.
	 * @param transition Transition, die hinzugefügt werden soll.
	 */
	public void createTransition(TransitionObject transition) {
		((StateObject) transition.getParentRenderable()).addTransition(transition);
		this.modified();
		this.automataPanel.refresh();
	}

	/**
	 * Modifiziert einen Zustand.
	 * @param state Zustand, der modifiziert werden soll.
	 * @param setInitial sagt aus, ob er initial sein soll.
	 * @param setFinalNew sagt aus, ob er final oder nicht final gesetzt werden soll.
	 * @param x neue x-Koordinate des Zustands.
	 * @param y neue y-Koordinate des Zustands.
	 */
	public void modifyState(StateObject state, boolean setInitial, boolean setFinalNew, int x, int y) {
		boolean hasChanged = false;
		if(state.isInitial() != true && setInitial) {
			state.setInitial(true);
			this.getFsmProgram().getStartState().setInitial(false);
			this.getFsmProgram().setStartState(state);
			hasChanged = true;
		}
		if(setFinalNew) {
			state.setFinal(!state.isFinal());
			hasChanged = true;
		}
		if(state.getXCoordinate() != x || state.getYCoordinate() != y) {
			state.setCoordinates(x, y);
			hasChanged = true;
		}
		if(hasChanged) {
			this.modified();
			this.automataPanel.refresh();
		}
	}

	/**
	 * Löscht einen Zustand, sowie alle dazugehörigen Transitions.
	 * @param state Zustand der gelöscht werden soll.
	 */
	public void deleteState(StateObject state) {
		this.getFsmProgram().removeState(state);
		for(StateObject transitionState : this.getFsmProgram().getAllStates()) {
			TransitionObject transition = transitionState.getTransitionWithToState(state);
			if(transition != null) {
				transitionState.removeChildFromParent(transition);
			}
		}
		this.modified();
		this.automataPanel.refresh();
	}

	/**
	 * Löscht einen Kommentar.
	 * @param comment Kommentar, der gelöscht werden soll.
	 */
	public void deleteComment(CommentObject comment) {
		this.getFsmProgram().removeComment(comment);
		this.modified();
		this.automataPanel.refresh();
	}

	/**
	 * Löscht ein Objekt und beachtet dabei ggf. Sonderfälle.
	 * @param object Objekt, welches gelöscht werden soll.
	 */
	@Override
	public void deleteObject(RenderableObject object) {
		if(object instanceof StateObject) {
			StateObject tempState = (StateObject) object;
			this.deleteState(tempState);
		} else if (object instanceof CommentObject) {
			CommentObject comment = (CommentObject) object;
			this.deleteComment(comment);
		}  else if (object instanceof TransitionDescriptionObject) {
			TransitionDescriptionObject tdo = (TransitionDescriptionObject) object;
			if(tdo.getParentRenderable().getChilds().size()<2) {
				JOptionPane.showMessageDialog(null, "Eine Beschriftung pro Transition muss"
						+ " bestehen bleiben!",
						"Endlicher Automat-Exception", JOptionPane.ERROR_MESSAGE,
						null);
			} else {
				tdo.getParentRenderable().removeChildFromParent(tdo);
				this.modified();
				this.getAutomataPanel().refresh();
			}
		} else if(object instanceof BooleanObject) {
				BooleanObject booleanObject = (BooleanObject) object;
				if(booleanObject.getParentRenderable() instanceof InputObject) {
					EpsilonBooleanObject newEpsilon = new EpsilonBooleanObject(0);
					((InputObject)booleanObject.getParentRenderable())
						.add(newEpsilon);
					this.modified();
					this.getAutomataPanel().refresh();
				} else {
					booleanObject.getParentRenderable().removeChildFromParent(booleanObject);
					this.modified();
					this.getAutomataPanel().refresh();
				}
		} else if(object instanceof VoidObject) {
			VoidObject voidObject = (VoidObject) object;
			if(voidObject.getParentRenderable().getChilds().size()<2) {
				voidObject.getParentRenderable().removeChildFromParent(voidObject);
				EpsilonFunctionObject newEpsilon = new EpsilonFunctionObject();
				voidObject.getParentRenderable().add(newEpsilon);
				this.modified();
				this.getAutomataPanel().refresh();
			} else {
				voidObject.getParentRenderable().removeChildFromParent(voidObject);
				this.modified();
				this.getAutomataPanel().refresh();
			}
		} else {
			FsmObject tmp = (FsmObject) object;
			tmp.getParentRenderable().removeChildFromParent(tmp);
			this.modified();
			this.getAutomataPanel().refresh();
		}
	}

	/**
	 * Setzt den Typ des endlichen Automats.
	 * @param isNondeterministic ist true, wenn der Automat nichtdeterminitisch ist.
	 */
	public void setTypeOfFsm(boolean isNondeterministic) {
		this.getFsmProgram().setTypeOfFsm(isNondeterministic);
		this.modified();
	}

	/**
	 * Ordnet den Graphen des endlichen Automaten an.
	 * Die Zustände werden dabei in einem Kreis angeordnet.
	 */
	public void layoutGraph() {
		double height = this.automataPanel.getHeight();
		double width = this.automataPanel.getWidth();
		double radius = 0;
		if(height < width) {
			radius= (height/2)-50;
		} else {
			radius= (width/2)-50;
		}
		double angle_step = 360.0/getFsmProgram().getAllStates().size();
		double angle = 0;
		
		for (StateObject state : getFsmProgram().getAllStates()) {
			state.setCoordinates(xCoordinate(radius,width,angle),
					yCoordinate(radius,height,angle));
			angle += angle_step;
			
		}
		this.modified();
		this.automataPanel.refresh();
		
	}

	/**
	 * Hilfsfunktion für Layout-Funktion um die X-Koordinate des Zustandes zu berechnen.
	 * @param radius Radius des Kreises auf dem die Zustände angeordnet werden sollen.
	 * @param width Breite des Panels.
	 * @param angle Winkel der für die Berechnung der X-Koordinate wichtig ist.
	 * @return
	 */
	private int xCoordinate(double radius, double width, double angle) {
		return (int) (Math.cos(Math.toRadians(angle)) * radius + width/2);
	}

	/**
	 * Hilfsfunktion für Layout-Funktion um die Y-Koordinate des Zustandes zu berechnen.
	 * @param radius Radius des Kreises auf dem die Zustände angeordnet werden sollen.
	 * @param height Höhe des Panels.
	 * @param angle Winkel der für die Berechnung der Y-Koordinate wichtig ist.
	 * @return
	 */
	private int yCoordinate(double radius, double height, double angle) {
		return (int) (Math.sin(Math.toRadians(angle)) * radius + height/2);
	}

	/**
	 * Zoomt an den endlichen Automaten heran.
	 */
	public void zoomIn() {
		if(getFsmProgram().getAllStates().size()>0) {
			getFsmProgram().getAllStates().get(0).setZoomFactor(true);
		}
		for(StateObject state : getFsmProgram().getAllStates()) {
			state.updateTextCoordinates();
		}
		for(CommentObject comment : getFsmProgram().getAllComments()) {
			comment.setTextCoordinates(comment.getName());
		}
		this.automataPanel.refresh();
	}

	/**
	 * Zoomt aus dem endlichen Automaten heraus.
	 */
	public void zoomOut() {
		if(getFsmProgram().getAllStates().size()>0) {
			getFsmProgram().getAllStates().get(0).setZoomFactor(false);
		}
		for(StateObject state : getFsmProgram().getAllStates()) {
			state.updateTextCoordinates();
		}
		for(CommentObject comment : getFsmProgram().getAllComments()) {
			comment.setTextCoordinates(comment.getName());
		}
		this.automataPanel.refresh();
	}

	/**
	 * Gibt die JScrollPane um das AutomataPanel zurück.
	 * @return
	 */
	public JScrollPane getScrollPane() {
		return this.scrollPane;
	}
	
	/**
	 * Setzt den EditModeButton.
	 */
	public void setEditModeSelected() {
		editMode.setSelected(true);
	}
}

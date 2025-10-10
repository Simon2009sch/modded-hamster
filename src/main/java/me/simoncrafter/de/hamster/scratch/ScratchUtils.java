package me.simoncrafter.de.hamster.scratch;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

import me.simoncrafter.de.hamster.scratch.Renderable.RType;
import me.simoncrafter.de.hamster.scratch.elements.BooleanMethodObject;
import me.simoncrafter.de.hamster.scratch.elements.VoidObject;
import me.simoncrafter.de.hamster.scratch.elements.booleans.AndBooleanObject;
import me.simoncrafter.de.hamster.scratch.elements.booleans.FalseBooleanObject;
import me.simoncrafter.de.hamster.scratch.elements.booleans.KornDaBooleanObject;
import me.simoncrafter.de.hamster.scratch.elements.booleans.MaulLeerBooleanObject;
import me.simoncrafter.de.hamster.scratch.elements.booleans.NotBooleanObject;
import me.simoncrafter.de.hamster.scratch.elements.booleans.OrBooleanObject;
import me.simoncrafter.de.hamster.scratch.elements.booleans.TrueBooleanObject;
import me.simoncrafter.de.hamster.scratch.elements.booleans.VornFreiBooleanObject;
import me.simoncrafter.de.hamster.scratch.elements.controls.DoWhileObject;
import me.simoncrafter.de.hamster.scratch.elements.controls.IfElseObject;
import me.simoncrafter.de.hamster.scratch.elements.controls.IfObject;
import me.simoncrafter.de.hamster.scratch.elements.controls.WhileObject;
import me.simoncrafter.de.hamster.scratch.elements.voids.GibVoidObject;
import me.simoncrafter.de.hamster.scratch.elements.voids.LinksUmVoidObject;
import me.simoncrafter.de.hamster.scratch.elements.voids.NimmVoidObject;
import me.simoncrafter.de.hamster.scratch.elements.voids.ReturnBooleanObject;
import me.simoncrafter.de.hamster.scratch.elements.voids.ReturnVoidObject;
import me.simoncrafter.de.hamster.scratch.elements.voids.VorVoidObject;
import me.simoncrafter.de.hamster.scratch.gui.InvalidIdentifierException;
import me.simoncrafter.de.hamster.workbench.Utils;
import me.simoncrafter.de.hamster.workbench.Workbench;

/**
 * Die ScratchUtils bieten einige n�tliche statische Funktionen, so
 * dass der Sourcecode minimiert wird.
 * @author HackZ
 *
 */
public class ScratchUtils {
	/**
	 * L�dt die Ressource als Bild von der �bergebenen
	 * Position <tt>name</tt>.
	 * @param name
	 * Lokale Refferenz zu der Ressource.
	 * @return
	 */
	public static BufferedImage getImage(String name) {
		Image img = Utils.getIcon(name).getImage();
		BufferedImage buffImg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics g = buffImg.getGraphics();
		g.drawImage(img, 0, 0, null);
		return buffImg;
		
//		File imgFile = new File("resources/" + name);
//		try {
//			return ImageIO.read(imgFile);
//		} catch (IOException e) {
//			System.out.println("Bild '" + name + "' konnten nicht geladen werden!");
//			e.printStackTrace();
//		}
//		return null;
	}
	
	/**
	 * Liefert die Breite zu dem �bergebenen Text mit der �bergebenen
	 * Schriftart.
	 * @param text
	 * Text zu dem die Breite gepr�ft werden soll.
	 * @param font
	 * Schriftart, mit der der Text geschrieben wird.
	 * @return
	 * Breite in Pixeln.
	 */
	public static int getTextWidth(String text, Font font) {
		BufferedImage temp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		Graphics g = temp.getGraphics();
		FontMetrics metrics = g.getFontMetrics(font);
	    return metrics.stringWidth(text);
	}
	
	/**
	 * Liefert die H�he zu dem �bergebenen Text mit der �bergebenen
	 * Schriftart.
	 * @param text
	 * Text zu dem die H�he gepr�ft werden soll.
	 * @param font
	 * Schriftart, mit der der Text geschrieben wird.
	 * @return
	 * H�he in Pixeln.
	 */
	public static int getTextHeight(String text, Font font) {
		BufferedImage temp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		Graphics g = temp.getGraphics();
		FontMetrics metrics = g.getFontMetrics(font);
	    return metrics.getHeight();
	}
	
	/**
	 * Liefert das WorkbenchFrame des HamsterSimulators.
	 * @return
	 */
	public static JFrame getWorkbenchFrame() {
		return Workbench.getWorkbench().getView().getEditorFrame();
	}
	
	/**
	 * �berpr�ft, ob der �bergebene text allen Javaidentifier 
	 * Konventionen entspricht.
	 * @param text
	 * text, der gepr�ft wird.
	 * @throws InvalidIdentifierException
	 * Wird geworfen, falls der Bezeichner nicht den
	 * Javakonventionen entspricht.
	 */
	public static void checkJavaIdentifier(String text) throws InvalidIdentifierException {
		if (text.equals(""))
			throw new InvalidIdentifierException("Der Bezeichner darf nicht leer sein!");
		
		if (!isIdentifierChar(text.charAt(0)))
			throw new InvalidIdentifierException("Der Bezeichner muss mit einem Buchstaben, Dollarzeichen oder Unterstrich beginnen!");
		
		for (int i = 1; i < text.length(); i++)
			if (!(isIdentifierChar(text.charAt(i)) || isNumeric(text.charAt(i))))
				throw new InvalidIdentifierException("Der Bezeichner enth�lt unzul�ssige Zeichen!");
	}
	
	/**
	 * �berpr�ft, ob der Buchtabe in einem Javaidentifier vorkommen darf
	 * @param c
	 * Zu pr�fender Buchstabe.
	 * @return
	 * true, wenn der Buchtabe verwendet werden darf.
	 */
	public static boolean isIdentifierChar(char c) {
		if (c == '$')
			return true;
		
		if (c == '_')
			return true;
		
		if (c >= 'a' && c <= 'z')
			return true;
		
		if (c >= 'A' && c <= 'Z')
			return true;
		
		return false;
	}
	
	/**
	 * �berpr�ft, ob der Character numerisch ist
	 * @param c
	 * Zu pr�fender Character
	 * @return
	 * true, wenn der Character numerisch ist (0-9).
	 */
	public static boolean isNumeric(char c) {
		if (c >= '0' && c <= '9')
			return true;
		
		return false;
	}
	
	/**
	 * Erstellt ein Redenerable, das dem �bergebenen Namen entspricht
	 * und liefert diesen zur�ck.
	 * @param name
	 * Name des Renderables
	 * @param type
	 * Typ des Renderables
	 * @return
	 */
	public static Renderable getRenderableByName(String name, String type) {
		// Feste voids
		if (name.equals("vor"))
			return new VorVoidObject();
		
		if (name.equals("linksUm"))
			return new LinksUmVoidObject();
		
		if (name.equals("nimm"))
			return new NimmVoidObject();
		
		if (name.equals("gib"))
			return new GibVoidObject();
		
		if (name.equals("return"))
			return new ReturnVoidObject();
		
		if (name.equals("returnB"))
			return new ReturnBooleanObject();
		
		// Feste booleans
		if (name.equals("vornFrei"))
			return new VornFreiBooleanObject();
		
		if (name.equals("kornDa"))
			return new KornDaBooleanObject();
		
		if (name.equals("maulLeer"))
			return new MaulLeerBooleanObject();
		
		if (name.equals("wahr"))
			return new TrueBooleanObject();
		
		if (name.equals("falsch"))
			return new FalseBooleanObject();
		
		if (name.equals("und"))
			return new AndBooleanObject();
		
		if (name.equals("oder"))
			return new OrBooleanObject();
		
		if (name.equals("nicht"))
			return new NotBooleanObject();
		
		// Controller
		if (name.equals("falls"))
			return new IfObject();
		
		if (name.equals("fallsSonst"))
			return new IfElseObject();

		if (name.equals("solange"))
			return new WhileObject();
		
		if (name.equals("tueSolange"))
			return new DoWhileObject();
		
		if (type.toUpperCase().equals("VOID"))
			return new VoidObject(name, new ArrayList<RType>());
		else
			return new BooleanMethodObject(name,  new ArrayList<RType>());
	}
	
	public static Renderable getRenderableByName(String name, RType rType) {
		switch (rType) {
		case VOID:
			return ScratchUtils.getRenderableByName(name, "VOID");
		default:
			return ScratchUtils.getRenderableByName(name, "BOOLEAN");
		}
	}
}

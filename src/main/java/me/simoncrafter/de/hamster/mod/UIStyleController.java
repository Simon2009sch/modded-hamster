package me.simoncrafter.de.hamster.mod;

import jscheme.JS;
import me.simoncrafter.de.hamster.editor.view.*;
import me.simoncrafter.de.hamster.editor.view.TextArea;
import me.simoncrafter.de.hamster.simulation.view.LogPanel;
import me.simoncrafter.de.hamster.simulation.view.SimulationPanel;

import javax.swing.*;
import javax.swing.plaf.SliderUI;
import javax.swing.plaf.basic.BasicSliderUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UIStyleController {
    private static Map<String, JComponent> uiComponents = new HashMap<>();

    private static JButton updateButton;

    public static void init() {
        modifyUIProperties("editor.filetree", (ui) -> {
            ui.setBackground(getRandomColor());
            ui.setForeground(getRandomColor());
            for (Component component : ui.getComponents()) {
                component.setBackground(getRandomColor());
            }
        });

        modifyUIProperties("editor.texteditor.linenumbers", (ui) -> {
            ui.setForeground(getRandomColor());
            ui.setBackground(getRandomColor());
            ((LineNumberPanel) ui).setSelectedTextColor(getRandomColor());
            ((LineNumberPanel) ui).setSelectionColor(getRandomColor());
        });

        modifyUIProperties("editor.texteditor.infobar.col.text", (ui) -> {
            ui.setBackground(getRandomColor());
        });

        modifyUIProperties("editor.texteditor.infobar.col.value", (ui) -> {
            ui.setBackground(getRandomColor());
        });

        modifyUIProperties("editor.texteditor.infobar.line.value", (ui) -> {
            ui.setBackground(getRandomColor());
        });

        modifyUIProperties("editor.texteditor.infobar.line.text", (ui) -> {
            ui.setBackground(getRandomColor());
        });

        modifyUIProperties("editor.texteditor", (ui) -> {
            TabbedTextArea editorText = (TabbedTextArea) ui;
            editorText.setBackground(getRandomColor());
            editorText.setForeground(getRandomColor());
            TextArea ac = editorText.getActiveTextArea();
            if (ac != null) {
                ac.setBackground(getRandomColor());
                JScrollPane scrollPane = editorText.getActiveScrollPlane();
                scrollPane.setBackground(getRandomColor());
                scrollPane.getVerticalScrollBar().setBackground(getRandomColor());
                scrollPane.getHorizontalScrollBar().setBackground(getRandomColor());
            }
            editorText.setOnTextAreaLock(textArea -> textArea.setBackground(Color.GRAY));
            editorText.setOnTextAreaUnLock(textArea -> textArea.setBackground(Color.WHITE));
        });



        if (updateButton == null) {
            updateButton = new JButton();
            updateButton.setText("Update");
            updateButton.setVisible(true);
            updateButton.addActionListener(e -> UIStyleController.init());
        }

        modifyUIProperties("editor.texteditor.infobar", (ui) -> {
            ui.setBackground(getRandomColor());
            ui.remove(updateButton);
            ui.add(updateButton, 0);
        });

        modifyUIPropertiesForPattern(".+\\.toolbar", (ui) -> {
            ui.setBackground(getRandomColor());
        });

        modifyUIPropertiesForPattern("^.+\\.toolbar\\.buttons\\.[^\\.]+$", (ui) -> {
            ui.setBackground(getRandomColor());
        });

        modifyUIProperties("editor.filetree.popupmenu", (ui) -> {
            JPopupMenu popupMenu = (JPopupMenu) ui;
            popupMenu.setBorder(BorderFactory.createLineBorder(getRandomColor(), 2));
            for (Component component : popupMenu.getComponents()) {
                component.setBackground(getRandomColor());
                component.setForeground(getRandomColor());
            }
        });

        modifyUIPropertiesForPattern("^.+\\.logpanel", (ui) -> {
            ui.setBackground(getRandomColor());
        });

        modifyUIPropertiesForPattern("^.+\\.logpanel.text", (ui) -> {
            ui.setBackground(getRandomColor());
            ui.setForeground(getRandomColor());
        });

        modifyUIProperties("simulation.panel", (ui) -> {
            ui.setBackground(getRandomColor());
        });

        modifyUIPropertiesForPattern("^.+\\.splitplane$", (ui) -> {
            JSplitPane plane = (JSplitPane) ui;
            plane.setUI(getSimpleUISlider(getRandomColor(), getRandomColor(), getRandomColor()));
        });

        modifyUIProperties("editor.menubar", (ui) -> {
            ui.setBackground(getRandomColor());
        });

        modifyUIProperties("editor.texteditor", (ui) -> {
            ui.setBackground(getRandomColor());
        });

        modifyUIPropertiesForPattern("^.+\\.debugger\\.toolbar\\.delay$", (ui) -> {
            JSlider slider = (JSlider) ui;
            slider.setUI(getSimpleSliderUI(slider, getRandomColor(), getRandomColor()));
            slider.setBackground(getRandomColor());
        });

    }

    public static void update() {
        init();
    }

    private static Color getRandomColor() {
        Random random = new Random();
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    public static BasicSliderUI getSimpleSliderUI(JSlider slider, Color fillColor, Color edgeColor) {
        return new BasicSliderUI(slider) {

            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g;
                super.paint(g, c);
            }

            @Override
            protected Dimension getThumbSize() {
                return new Dimension(20, 28);
            }

            @Override
            public void paintTrack(Graphics g) {
                /*Graphics2D g2d = (Graphics2D) g;
                Stroke old = g2d.getStroke();
                g2d.setStroke(stroke);
                g2d.setPaint(Color.BLACK);
                if (slider.getOrientation() == SwingConstants.HORIZONTAL) {
                    g2d.drawLine(trackRect.x, trackRect.y + trackRect.height / 2,
                            trackRect.x + trackRect.width, trackRect.y + trackRect.height / 2);
                } else {
                    g2d.drawLine(trackRect.x + trackRect.width / 2, trackRect.y,
                            trackRect.x + trackRect.width / 2, trackRect.y + trackRect.height);
                }
                g2d.setStroke(old);*/
            }

            @Override
            public void paintThumb(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;

                slider.setPaintTicks(false);

                // Coordinates and size
                int x = thumbRect.x + 2;
                int y = thumbRect.y + 2;
                int size = 8;
                int boxWidth = thumbRect.width * 100;

                // Draw filled square
                g2d.setPaint(fillColor);
                g2d.fillRect(x - boxWidth, y + 5, size + boxWidth, size + 5);

            }
        };
    }

    public static BasicSplitPaneUI getSimpleUISlider(Color defaultColor, Color highlightColor, Color handleColor) {
        return new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    private boolean hovered = false;
                    {
                        // Remove default arrow buttons
                        for (Component c : getComponents()) {
                            c.setVisible(false);
                        }
                        setLayout(null);
                        setBackground(new Color(100, 60, 60));

                        // Detect hover to show highlight
                        addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseEntered(MouseEvent e) {
                                hovered = true;
                                repaint();
                            }

                            @Override
                            public void mouseExited(MouseEvent e) {
                                hovered = false;
                                repaint();
                            }
                        });
                    }

                    @Override
                    public void paint(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        // Background color
                        g2.setColor(defaultColor);
                        g2.fillRect(0, 0, getWidth(), getHeight());

                        // Hover highlight
                        if (hovered) {
                            g2.setColor(highlightColor); // Windows blue highlight
                            if (splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
                                g2.fillRect(0, 0, getWidth(), getHeight());
                            } else {
                                g2.fillRect(0, 0, getWidth(), getHeight());
                            }
                        }

                        // Optional: small grip line in center
                        g2.setColor(handleColor);
                        if (splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
                            int y = getHeight() / 2 - 10;
                            g2.fillRoundRect(getWidth() / 2 - 1, y, 2, 20, 2, 2);
                        } else {
                            int x = getWidth() / 2 - 10;
                            g2.fillRoundRect(x, getHeight() / 2 - 1, 20, 2, 2, 2);
                        }
                    }
                };
            }
        };
    }

    private static void modifyUIProperties(String key, Consumer<JComponent> operation) {
        if (uiComponents == null || uiComponents.get(key) == null) {
            return;
        }
        operation.accept(uiComponents.get(key));
    }

    private static void modifyUIPropertiesForPattern(String regex, Consumer<JComponent> operation) {
        if (uiComponents == null) {
            return;
        }
        for (String key : uiComponents.keySet()) {
            if (Pattern.matches(regex, key) && uiComponents.get(key) != null) {
                operation.accept(uiComponents.get(key));
            }
        }
    }


    public static void putUIComponent(String key, JComponent component) {
        uiComponents.put(key, component);
        UIStyleController.update();
    }

}

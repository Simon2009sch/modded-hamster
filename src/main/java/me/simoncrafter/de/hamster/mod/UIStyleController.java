package me.simoncrafter.de.hamster.mod;

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

public class UIStyleController {

    private static Map<String, JComponent> uiComponents = new HashMap<>();

    private static LineNumberPanel lineNumberPanel;
    private static FileTree fileTree;
    private static JTextField editorInfoBarColText;
    private static JTextField editorInfoBarColVal;
    private static JTextField editorInfoBarLineText;
    private static JTextField editorInfoBarLineVal;
    private static TabbedTextArea editorTextAreaBackground;
    private static JPanel editorInfoBar;
    private static LogPanel logPanel;
    private static JTextPane logPanelText;
    private static List<JToolBar> toolBars = new ArrayList<>();
    private static List<JToolBar> sliderToolBars = new ArrayList<>();
    private static List<JSlider> sliders = new ArrayList<>();
    private static JPopupMenu filePopupMenu;
    private static JToolBar editorToolBar;
    private static SimulationPanel simulationPanel;
    private static List<JButton> buttons = new ArrayList<>();
    private static List<JToggleButton> toggleButtons = new ArrayList<>();
    private static JMenuBar editorMenuBar;
    private static List<JSplitPane> splitPlanes = new ArrayList<>();

    private static JButton updateButton;

    public static void init() {
        //uiComponents.get("editor.filetree").

        if (fileTree != null) {
            fileTree.setBackground(getRandomColor());
            fileTree.setForeground(getRandomColor());
            for (Component component : fileTree.getComponents()) {
                component.setBackground(getRandomColor());
            }
        }

        if (lineNumberPanel != null) {
            lineNumberPanel.setForeground(getRandomColor()); // number color
            lineNumberPanel.setBackground(getRandomColor());
            lineNumberPanel.setSelectionColor(getRandomColor());
            lineNumberPanel.setSelectedTextColor(getRandomColor());
        }


        if (editorInfoBarColText != null) {
            editorInfoBarColText.setBackground(getRandomColor());
        }
        if (editorInfoBarColVal != null) {
            editorInfoBarColVal.setBackground(getRandomColor());
        }
        if (editorInfoBarLineText != null) {
            editorInfoBarLineText.setBackground(getRandomColor());
        }
        if (editorInfoBarLineVal != null) {
            editorInfoBarLineVal.setBackground(getRandomColor());
        }
        if (editorTextAreaBackground != null) {
            editorTextAreaBackground.setBackground(getRandomColor());
            editorTextAreaBackground.setForeground(getRandomColor());
            TextArea ac = editorTextAreaBackground.getActiveTextArea();
            if (ac != null) {
                ac.setBackground(getRandomColor());
                JScrollPane scrollPane = editorTextAreaBackground.getActiveScrollPlane();
                scrollPane.setBackground(getRandomColor());
                scrollPane.getVerticalScrollBar().setBackground(getRandomColor());
                scrollPane.getHorizontalScrollBar().setBackground(getRandomColor());
            }
            editorTextAreaBackground.setOnTextAreaLock(textArea -> textArea.setBackground(Color.GRAY));
            editorTextAreaBackground.setOnTextAreaUnLock(textArea -> textArea.setBackground(Color.WHITE));
        }

        if (updateButton == null) {
            updateButton = new JButton();
            updateButton.setText("Update");
            updateButton.setVisible(true);
            updateButton.addActionListener(e -> UIStyleController.init());
        }
        if (editorInfoBar != null) {
            editorInfoBar.remove(updateButton);
            editorInfoBar.add(updateButton, 0);
            editorInfoBar.setBackground(getRandomColor());
        }

        toolBars.forEach(toolBar -> {
            toolBar.setBackground(getRandomColor());
        });


        if (filePopupMenu != null) {
            filePopupMenu.setBorder(BorderFactory.createLineBorder(getRandomColor(), 2));
            for (Component component : filePopupMenu.getComponents()) {
                component.setBackground(getRandomColor());
                component.setForeground(getRandomColor());
            }
        }

        if (editorInfoBar != null) {
            editorInfoBar.setBackground(getRandomColor());
        }

        if (logPanel != null) {
            logPanel.setBackground(getRandomColor());
        }

        if (logPanelText != null) {
            logPanelText.setBackground(getRandomColor());
            logPanelText.setForeground(getRandomColor());
        }

        if (editorToolBar != null) {
            editorToolBar.setBackground(getRandomColor());
        }

        if (simulationPanel != null) {
            simulationPanel.setBackground(getRandomColor());
        }

        toggleButtons.forEach(button -> {
            button.setBackground(getRandomColor());
        });
        buttons.forEach(button -> {
            button.setBackground(getRandomColor());
        });

        splitPlanes.forEach(plane -> {
            plane.setUI(getSimpleUISlider(getRandomColor(), getRandomColor(), getRandomColor()));
        });

        if (editorMenuBar != null) {
            editorMenuBar.setBackground(getRandomColor());
        }

        sliderToolBars.forEach(bar -> bar.setBackground(getRandomColor()));
        sliders.forEach(slider -> slider.setUI(getSimpleSliderUI(slider, getRandomColor(), getRandomColor())));
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
            private BasicStroke stroke = new BasicStroke(1f, BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND, 0f, new float[]{1f, 2f}, 0f);

            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                super.paint(g, c);
            }

            @Override
            protected Dimension getThumbSize() {
                return new Dimension(12, 16);
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

                // Coordinates and size
                int x = thumbRect.x + 2;
                int y = thumbRect.y + 2;
                int size = Math.min(thumbRect.width, thumbRect.height) - 4;
                int boxWidth = thumbRect.width * 100;

                // Draw filled square
                g2d.setPaint(fillColor);
                g2d.fillRect(x - boxWidth, y, size + boxWidth, size + 5);

                // Draw border
                Stroke old = g2d.getStroke();
                g2d.setStroke(new BasicStroke(2f));
                g2d.setPaint(edgeColor);
                g2d.drawRect(x - boxWidth, y, size + boxWidth, size + 5);
                g2d.setStroke(old);
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

    public static void putNewUiElement(String key, JComponent newComponent) {
        uiComponents.put(key, newComponent);
        UIStyleController.update();
    }

    public static void setEditorMenuBar(JMenuBar editorMenuBar) {
        UIStyleController.editorMenuBar = editorMenuBar;
        UIStyleController.update();
    }

    public static void setLogPanelText(JTextPane logPanelText) {
        UIStyleController.logPanelText = logPanelText;
        UIStyleController.update();
    }

    public static void setSimulationPanel(SimulationPanel simulationPanel) {
        UIStyleController.simulationPanel = simulationPanel;
        UIStyleController.update();
    }

    public static void setLineNumberPanel(LineNumberPanel lineNumberPanel) {
        UIStyleController.lineNumberPanel = lineNumberPanel;
        UIStyleController.update();
    }

    public static void setFilePopupMenu(JPopupMenu filePopupMenu) {
        UIStyleController.filePopupMenu = filePopupMenu;
        UIStyleController.update();

    }

    public static void setFileTree(FileTree fileTree) {
        UIStyleController.fileTree = fileTree;
        UIStyleController.update();
    }

    public static void setButtons(List<JButton> buttons) {
        UIStyleController.buttons = buttons;
        UIStyleController.update();
    }

    public static void addButton(JButton button) {
        UIStyleController.buttons.add(button);
        UIStyleController.update();
    }

    public static void setToggleButtons(List<JToggleButton> toggleButtons) {
        UIStyleController.toggleButtons = toggleButtons;
        UIStyleController.update();
    }

    public static void addToggleButton(JToggleButton button) {
        UIStyleController.toggleButtons.add(button);
    }

    public static void setEditorInfoBarColText(JTextField editorInfoBarColText) {
        UIStyleController.editorInfoBarColText = editorInfoBarColText;
        UIStyleController.update();
    }

    public static void setEditorToolBar(JToolBar editorToolBar) {
        UIStyleController.editorToolBar = editorToolBar;
        UIStyleController.update();
    }

    public static void setEditorInfoBarColVal(JTextField editorInfoBarColVal) {
        UIStyleController.editorInfoBarColVal = editorInfoBarColVal;
        UIStyleController.update();
    }

    public static void setEditorInfoBarLineText(JTextField editorInfoBarLineText) {
        UIStyleController.editorInfoBarLineText = editorInfoBarLineText;
        UIStyleController.update();
    }

    public static void setEditorInfoBarLineVal(JTextField editorInfoBarLineVal) {
        UIStyleController.editorInfoBarLineVal = editorInfoBarLineVal;
        UIStyleController.update();
    }

    public static void setEditorTextAreaBackground(TabbedTextArea editorTextAreaBackground) {
        UIStyleController.editorTextAreaBackground = editorTextAreaBackground;
        UIStyleController.update();
    }

    public static void setLogPanel(LogPanel logPanel) {
        UIStyleController.logPanel = logPanel;
        UIStyleController.update();
    }

    public static void setToolBars(List<JToolBar> toolBars) {
        UIStyleController.toolBars = toolBars;
        UIStyleController.update();
    }

    public static void addToolBar(JToolBar toolBar) {
        UIStyleController.toolBars.add(toolBar);
        UIStyleController.update();
    }

    public static void setEditorInfoBar(JPanel editorInfoBar) {
        UIStyleController.editorInfoBar = editorInfoBar;
        UIStyleController.update();
    }

    public static void setSplitPlanes(List<JSplitPane> splitPlanes) {
        UIStyleController.splitPlanes = splitPlanes;
        UIStyleController.update();
    }

    public static void addSplitPlane(JSplitPane splitPlane) {
        UIStyleController.splitPlanes.add(splitPlane);
        UIStyleController.update();
    }

    public static void setSliderToolBars(List<JToolBar> sliderToolBars) {
        UIStyleController.sliderToolBars = sliderToolBars;
        UIStyleController.update();
    }

    public static void addSliderToolBar(JToolBar toolBar) {
        UIStyleController.sliderToolBars.add(toolBar);
        UIStyleController.update();
    }

    public static void setSliders(List<JSlider> sliders) {
        UIStyleController.sliders = sliders;
        UIStyleController.update();
    }

    public static void addSlider(JSlider slider) {
        UIStyleController.sliders.add(slider);
        UIStyleController.update();
    }
}

package me.simoncrafter.de.hamster.styles.controller;

import me.simoncrafter.de.hamster.editor.view.*;
import me.simoncrafter.de.hamster.editor.view.TextArea;
import me.simoncrafter.de.hamster.simulation.view.SimulationPanel;
import me.simoncrafter.de.hamster.styles.model.UIColorStyle;
import me.simoncrafter.de.hamster.workbench.Utils;
import org.joda.time.field.UnsupportedDateTimeField;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.BasicSliderUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class UIStyleController {
    private static Map<String, JComponent> uiComponents = new HashMap<>();

    private static String selectedStyleName = "darkMode";

    public static void applyStyle(boolean reload) {
        Map<String, UIColorStyle> styleMap = StyleSettings.getColorStyles();
        // Apply all styles in the map
        UIColorStyle s = styleMap.get(selectedStyleName);
        UIColorStyle style;
        if (s == null) {
            style = ((List<UIColorStyle>) Utils.collectionToArray(styleMap.values())).get(0);
        } else {
            style = s;
        }

        for (Map.Entry<String, Object> entry : style.getColors().entrySet()) {
            if (entry.getKey().startsWith("!")) {
                modifyUIPropertiesForPattern(entry.getKey().substring(1), (ui, key) -> {
                    style.apply(ui, key, entry.getKey(), reload);
                });
            } else {
                modifyUIProperties(entry.getKey(), (ui) -> {
                    style.apply(ui, entry.getKey(), entry.getKey(), reload);
                });
            }
        }

    }

    public static void setSelectedStyle(String name) {
        selectedStyleName = name;
    }

    public static void setSelectedStyleNameAndUpdate(String name) {
        setSelectedStyle(name);
        applyStyle(false);
    }


    public static void setRandomColorToEverything() {
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

                scrollPane.getVerticalScrollBar().setUI(getBasicScrollBar(getRandomColor(), 5));
                scrollPane.getHorizontalScrollBar().setUI(getBasicScrollBar(getRandomColor(), 5));



            }
            editorText.setOnTextAreaLock(textArea -> textArea.setBackground(Color.GRAY));
            editorText.setOnTextAreaUnLock(textArea -> textArea.setBackground(Color.WHITE));
        });


        modifyUIPropertiesForPattern(".+\\.toolbar", (ui, key) -> {
            ui.setBackground(getRandomColor());
        });

        modifyUIPropertiesForPattern("^.+\\.toolbar\\.buttons\\.[^\\.]+$", (ui, key) -> {
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

        modifyUIPropertiesForPattern("^.+\\.logpanel", (ui, key) -> {
            ui.setBackground(getRandomColor());
        });

        modifyUIPropertiesForPattern("^.+\\.logpanel.text", (ui, key) -> {
            ui.setBackground(getRandomColor());
            ui.setForeground(getRandomColor());
        });

        modifyUIProperties("simulation.panel", (ui) -> {
            SimulationPanel simPanel = (SimulationPanel) ui;
            simPanel.setBackground(getRandomColor());
        });

        modifyUIProperties("simulation.panel.scroll", (ui) -> {
            JScrollPane scrollPane = (JScrollPane) ui;
            JScrollBar vScroll = scrollPane.getVerticalScrollBar();
            JScrollBar hScroll = scrollPane.getHorizontalScrollBar();

            vScroll.setUI(getBasicScrollBar(getRandomColor(), 5));
            vScroll.setUnitIncrement(16);

            hScroll.setUI(getBasicScrollBar(getRandomColor(), 5));
            hScroll.setUnitIncrement(16);

            scrollPane.setBackground(getRandomColor());
        });

        modifyUIPropertiesForPattern("^.+\\.splitplane$", (ui, key) -> {
            JSplitPane plane = (JSplitPane) ui;
            plane.setUI(getSimpleSplitPlane(getRandomColor(), getRandomColor(), getRandomColor()));
        });

        modifyUIProperties("editor.menubar", (ui) -> {
            ui.setBackground(getRandomColor());
        });

        modifyUIProperties("editor.texteditor", (ui) -> {
            ui.setBackground(getRandomColor());
        });

        modifyUIPropertiesForPattern("^.+\\.debugger\\.toolbar\\.delay$", (ui, key) -> {
            JSlider slider = (JSlider) ui;
            slider.setUI(getSimpleSliderUI(slider, getRandomColor(), getRandomColor(), 10, 2));
            slider.setBackground(getRandomColor());
        });
    }

    public static void update() {
        applyStyle(false);
    }

    private static Color getRandomColor() {
        Random random = new Random();
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    public static BasicScrollBarUI getBasicScrollBar(Color thunb, int thickness) {
        return new BasicScrollBarUI() {
            private final Color THUMB_COLOR = thunb;
            private final int MIN_SIZE = 10;       // Minimum thumb dimension
            private final double SIZE_SCALE = 0.6; // Makes thumb larger overall
            private final int TRACK_THICKNESS = thickness; // Width/thickness of scrollbar track

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                if (!c.isEnabled() || thumbBounds.isEmpty()) return;

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(THUMB_COLOR);

                if (scrollbar.getOrientation() == Adjustable.VERTICAL) {
                    int range = scrollbar.getMaximum() - scrollbar.getVisibleAmount();
                    int trackHeight = thumbBounds.height;

                    // Scale thumb height
                    int thumbHeight = (int) (scrollbar.getVisibleAmount() * SIZE_SCALE);
                    thumbHeight = Math.max(MIN_SIZE, Math.min(thumbHeight, trackHeight));

                    // Position so it's anchored top/bottom properly
                    int y;
                    if (range <= 0) {
                        y = thumbBounds.y;
                        thumbHeight = trackHeight;
                    } else {
                        float scrollRatio = (float) scrollbar.getValue() / range;
                        int available = trackHeight - thumbHeight;
                        y = thumbBounds.y + Math.round(available * scrollRatio);
                    }

                    // Make the thumb wider — override its width
                    int barWidth = Math.max(TRACK_THICKNESS, thumbBounds.width);
                    int x = thumbBounds.x + (thumbBounds.width - barWidth) / 2;

                    // Draw larger, rounder handle
                    g2.fillRoundRect(x, y, barWidth, thumbHeight, 8, 8);

                } else { // Horizontal scrollbar
                    int range = scrollbar.getMaximum() - scrollbar.getVisibleAmount();
                    int trackWidth = thumbBounds.width;

                    int thumbWidth = (int) (scrollbar.getVisibleAmount() * SIZE_SCALE);
                    thumbWidth = Math.max(MIN_SIZE, Math.min(thumbWidth, trackWidth));

                    int x;
                    if (range <= 0) {
                        x = thumbBounds.x;
                        thumbWidth = trackWidth;
                    } else {
                        float scrollRatio = (float) scrollbar.getValue() / range;
                        int available = trackWidth - thumbWidth;
                        x = thumbBounds.x + Math.round(available * scrollRatio);
                    }

                    int barHeight = Math.max(TRACK_THICKNESS, thumbBounds.height);
                    int y = thumbBounds.y + (thumbBounds.height - barHeight) / 2;

                    g2.fillRoundRect(x, y, thumbWidth, barHeight, 8, 8);
                }
                scrollbar.setOpaque(false);
                g2.dispose();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                //g.setColor(new Color(235, 235, 235));
                //g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }

            @Override
            protected void installComponents() {
                super.installComponents();
                if (scrollbar != null) {
                    scrollbar.setOpaque(false);
                    scrollbar.setBackground(new Color(0, 0, 0, 0));
                }
            }
        };
    }

    public static BasicSliderUI getSimpleSliderUI(JSlider slider, Color fillColor, Color edgeColor, int thickness, int edgeThickness) {
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
                //g2d.setStroke(stroke);
                g2d.setPaint(Color.RED);
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
                int size = 1;
                int boxWidth = thumbRect.width * 100;

                g2d.setPaint(edgeColor);
                g2d.fillRect(x - boxWidth - edgeThickness/2, y + thickness/2 - edgeThickness/2, size + boxWidth + edgeThickness, thickness + edgeThickness);

                // Draw filled square
                g2d.setPaint(fillColor);
                g2d.fillRect(x - boxWidth, y + 5, size + boxWidth, thickness);

            }
        };
    }

    public static BasicSplitPaneUI getSimpleSplitPlane(Color defaultColor, Color highlightColor, Color handleColor) {
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

    public static void modifyUIProperties(String key, Consumer<JComponent> operation) {
        if (uiComponents == null || uiComponents.get(key) == null) {
            System.out.println("Key not found: " + key);
            return;
        }
        operation.accept(uiComponents.get(key));
    }

    public static void modifyUIPropertiesForPattern(String regex, BiConsumer<JComponent, String> operation) {
        if (uiComponents == null) {
            return;
        }
        for (String key : uiComponents.keySet()) {
            if (Pattern.matches(regex, key) && uiComponents.get(key) != null) {
                operation.accept(uiComponents.get(key), key);
            }
        }
    }


    public static void putUIComponent(String key, JComponent component) {
        uiComponents.put(key, component);
        //UIStyleController.update();
    }

}

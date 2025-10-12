package me.simoncrafter.de.hamster.mod;

import me.simoncrafter.de.hamster.editor.view.FileTree;
import me.simoncrafter.de.hamster.editor.view.LineNumberPanel;
import me.simoncrafter.de.hamster.editor.view.TabbedTextArea;
import me.simoncrafter.de.hamster.editor.view.TextArea;
import me.simoncrafter.de.hamster.simulation.view.LogPanel;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class UIStyleController {
    private static LineNumberPanel lineNumberPanel;
    private static FileTree fileTree;
    private static JTextField editorInfoBarColText;
    private static JTextField editorInfoBarColVal;
    private static JTextField editorInfoBarLineText;
    private static JTextField editorInfoBarLineVal;
    private static TabbedTextArea editorTextAreaBackground;
    private static JPanel editorInfoBar;
    private static LogPanel logPanel;
    private static JToolBar toolBar;
    private static JPopupMenu filePopupMenu;

    private static JButton updateButton;

    public static void init() {
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

        if (toolBar != null) {
            toolBar.setBackground(getRandomColor());
        }

        if (filePopupMenu != null) {
            filePopupMenu.setBorder(BorderFactory.createLineBorder(getRandomColor(), 2));
            for (Component component : filePopupMenu.getComponents()) {
                component.setBackground(getRandomColor());
                component.setForeground(getRandomColor());
            }
        }

    }

    public static void update() {
        init();
    }

    private static Color getRandomColor() {
        Random random = new Random();
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    public static BasicSplitPaneUI getSimpleUISlider() {
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
                        g2.setColor(Color.RED);
                        g2.fillRect(0, 0, getWidth(), getHeight());

                        // Hover highlight
                        if (hovered) {
                            g2.setColor(Color.RED); // Windows blue highlight
                            if (splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
                                g2.fillRect(0, 0, getWidth(), getHeight());
                            } else {
                                g2.fillRect(0, 0, getWidth(), getHeight());
                            }
                        }

                        // Optional: small grip line in center
                        g2.setColor(Color.RED);
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

    public static void setLineNumberPanel(LineNumberPanel lineNumberPanel) {
        UIStyleController.lineNumberPanel = lineNumberPanel;
    }

    public static void setFilePopupMenu(JPopupMenu filePopupMenu) {
        UIStyleController.filePopupMenu = filePopupMenu;
    }

    public static void setFileTree(FileTree fileTree) {
        UIStyleController.fileTree = fileTree;
    }

    public static void setEditorInfoBarColText(JTextField editorInfoBarColText) {
        UIStyleController.editorInfoBarColText = editorInfoBarColText;
    }

    public static void setEditorInfoBarColVal(JTextField editorInfoBarColVal) {
        UIStyleController.editorInfoBarColVal = editorInfoBarColVal;
    }

    public static void setEditorInfoBarLineText(JTextField editorInfoBarLineText) {
        UIStyleController.editorInfoBarLineText = editorInfoBarLineText;
    }

    public static void setEditorInfoBarLineVal(JTextField editorInfoBarLineVal) {
        UIStyleController.editorInfoBarLineVal = editorInfoBarLineVal;
    }

    public static void setEditorTextAreaBackground(TabbedTextArea editorTextAreaBackground) {
        UIStyleController.editorTextAreaBackground = editorTextAreaBackground;
    }

    public static void setLogPanel(LogPanel logPanel) {
        UIStyleController.logPanel = logPanel;
    }

    public static void setToolBar(JToolBar toolBar) {
        UIStyleController.toolBar = toolBar;
    }

    public static void setEditorInfoBar(JPanel editorInfoBar) {
        UIStyleController.editorInfoBar = editorInfoBar;
    }
}

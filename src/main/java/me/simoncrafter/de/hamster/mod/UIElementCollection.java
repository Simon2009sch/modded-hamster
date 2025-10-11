package me.simoncrafter.de.hamster.mod;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UIElementCollection {
    public static final BasicSplitPaneUI SLIDER = new BasicSplitPaneUI() {
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
                    g2.setColor(ColorManager.getCurrent().getSliderColor());
                    g2.fillRect(0, 0, getWidth(), getHeight());

                    // Hover highlight
                    if (hovered) {
                        g2.setColor(ColorManager.getCurrent().getSliderColor().darker()); // Windows blue highlight
                        if (splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
                            g2.fillRect(0, 0, getWidth(), getHeight());
                        } else {
                            g2.fillRect(0, 0, getWidth(), getHeight());
                        }
                    }

                    // Optional: small grip line in center
                    g2.setColor(ColorManager.getCurrent().getSliderColor().brighter());
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

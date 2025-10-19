package me.simoncrafter.de.hamster.styles.model;


import com.intel.bluetooth.obex.OBEXClientOperation;
import me.simoncrafter.de.hamster.simulation.view.multimedia.opengl.objects.Obj;
import org.python.antlr.op.In;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class UIColorStyle {
    private Map<String, Object> colors;
    private String name;
    private List<Consumer<JComponent>> applyFunctions = new ArrayList<>();

    public UIColorStyle(Map<String, Object> colors, String name) {
        this.colors = colors;
        this.name = name;
    }

    public Map<String, Object> getColors() {
        return colors;
    }

    public void setColors(Map<String, Object> colors) {
        this.colors = colors;
    }

    public void putColor(String key, Object value) {
        colors.put(key, value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void apply(JComponent component, String key, boolean reload) {
        if (applyFunctions.isEmpty() || reload) {
            applyRecursive(key, colors);
        }
        for (Consumer<JComponent> consumer : applyFunctions) {
            consumer.accept(component);
        }
    }
    public void apply(JComponent component, String key) {
        apply(component, key, false);
    }

    private void applyRecursive(Map<String, Object> map, Function<Consumer<JComponent>, Consumer<JComponent>> parent) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            switch (key) {
                case "bg": {
                    if (value instanceof Color) {
                        applyFunctions.add(parent.apply(ui -> ui.setBackground((Color) value)));
                    }
                }
                case "fg": {
                    if (value instanceof Color) {
                        applyFunctions.add(parent.apply(ui -> ui.setForeground((Color) value)));
                    }
                }
                case "border": {
                    if (value instanceof Map<?, ?>) {
                        applyBorder((Map<String, Object>) value);
                    }
                }
                case "item": {
                    if (value instanceof Map<?, ?>) {
                        applyRecursive((Map<String, Object>) value, parent);
                    }
                }
                default: {
                    // handle other keys if needed
                }
            }
        }
    }

    private void applyBorder(Map<String, Object> map) {
        if (map.isEmpty()) {
            applyFunctions.add(ui -> {ui.setBorder(BorderFactory.createEmptyBorder());});
            return;
        }

        Object th = map.get("th");
        Object color = map.get("color");
        if (!(th instanceof Integer)) {
            th = 2;
        }
        if (!(color instanceof Color)) {
            color = Color.BLACK;
        }
        Object finalColor = color;
        Object finalTh = th;
        applyFunctions.add(ui -> {ui.setBorder(BorderFactory.createLineBorder((Color) finalColor, (Integer) finalTh));});
    }

}

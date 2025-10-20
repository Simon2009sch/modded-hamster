package me.simoncrafter.de.hamster.styles.model;


import com.intel.bluetooth.obex.OBEXClientOperation;
import me.simoncrafter.de.hamster.simulation.view.multimedia.opengl.objects.Obj;
import org.python.antlr.op.In;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class UIColorStyle {
    private Map<String, Object> colors;
    private String name;
    private Map<String, List<Consumer<JComponent>>> applyFunctions = new HashMap<>();

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
            createApplierFunction((Map<String, Object>) colors.get(key), key, );
        }
        for (Consumer<JComponent> consumer : applyFunctions.get(key)) {
            consumer.accept(component);
        }
    }
    public void apply(JComponent component, String key) {
        apply(component, key, false);
    }

    private Consumer<JComponent> createApplierFunction(boolean addToList, Map<String, Object> map, String key, Consumer<Consumer<JComponent>> parent) {
        if ((map.get(key)) instanceof Color) {
            Consumer<JComponent> consumer = (ui) -> {
                ui.setBackground((Color) map.get(key));
            };
            if (addToList) addToAplierViaKey(key, consumer);
            return consumer;
        } else if (!((map.get(key)) instanceof Color)) {
            return (ui) -> {};
        }

        Map<String, Object> colorableObject = (Map<String, Object>) map.get(key);

        for (Map.Entry<String, Object> entry : colorableObject.entrySet()) {
            Object eObj = entry.getValue();
            String eKey = entry.getKey();

            switch (eKey) {
                case "bg": {
                    Consumer<JComponent> consumer = (ui) -> {
                        parent.accept(pui -> pui.setBackground((Color) eObj));
                    };
                    if (addToList) addToAplierViaKey(key, consumer);
                    return consumer;
                }
                case "fg": {
                    Consumer<JComponent> consumer = (ui) -> {
                        parent.accept(pui -> pui.setForeground((Color) eObj));
                    };
                    if (addToList) addToAplierViaKey(key, consumer);
                    return consumer;
                }
                case "item": {
                    Consumer<JComponent> child = createApplierFunction(false, (Map<String, Object>) eObj, "item", parent);
                    Consumer<JComponent> consumer = (ui) -> {
                        parent.accept(child::accept);
                    };
                    if (addToList) addToAplierViaKey(key, consumer);
                    return consumer;
                }
                case "border": {

                }
            }
        }
    }

    private void addToAplierViaKey(String key, Consumer<JComponent> consumer) {
        List<Consumer<JComponent>> conList = applyFunctions.get(key);
        conList.add(consumer);
        applyFunctions.put(key, conList);
    }


    private Consumer<JComponent> applyBorder(Map<String, Object> map) {
        if (map.isEmpty()) {
            return (ui) -> ui.setBorder(BorderFactory.createEmptyBorder());
        }

        Object style = map.get("style");
        switch ((String) style) {
            case "empty": {

            }
            case "line": {
                int thickness = Integer.parseInt(map.get("thickness").toString());
                Color color = (Color) map.get("color");

                return (ui) -> ui.setBorder(BorderFactory.createLineBorder(color, thickness));
            }
        }
    }

}

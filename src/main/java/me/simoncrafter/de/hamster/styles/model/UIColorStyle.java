package me.simoncrafter.de.hamster.styles.model;


import com.intel.bluetooth.obex.OBEXClientOperation;
import com.sun.istack.internal.Nullable;
import jsint.E;
import me.simoncrafter.de.hamster.editor.view.FileTree;
import me.simoncrafter.de.hamster.editor.view.FileTreeCellRenderer;
import me.simoncrafter.de.hamster.editor.view.TabbedTextArea;
import me.simoncrafter.de.hamster.editor.view.TextArea;
import me.simoncrafter.de.hamster.simulation.view.multimedia.opengl.objects.Obj;
import org.apache.bcel.classfile.ConstantUtf8;
import org.jruby.RubyProcess;
import org.python.antlr.op.In;

import javax.swing.*;
import javax.swing.text.JTextComponent;
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

    public void apply(JComponent component, String key, String savedKey, boolean reload) {
        if (applyFunctions.get(key) == null || applyFunctions.get(key).isEmpty() || reload) {
            // now call createApplierFunction properly
            createApplierFunction(true, colors, key, savedKey, null);
        }
        if (applyFunctions.get(savedKey) == null) {
            System.out.println("Couldn't load/find " + key);
            return;
        }
        for (Consumer<JComponent> consumer : applyFunctions.get(savedKey)) {
            consumer.accept(component);
        }
    }
    public void apply(JComponent component, String key) {
        apply(component, key, key, false);
    }
    public void apply(JComponent component, String key, String savedKey) {
        apply(component, key, savedKey, false);
    }

    private Consumer<JComponent> createApplierFunction(
            boolean addToList,
            Map<String, Object> map,
            String key,
            String savedKey,
            @Nullable Consumer<Consumer<JComponent>> parent
    ) {
        Object value = map.get(savedKey);

        // Case 1: Simple color — just set background
        if (value instanceof Color) {
            Consumer<JComponent> consumer = ui -> ui.setBackground((Color) value);
            if (addToList) addToAplierViaKey(key, consumer);
            return consumer;
        }

        // Case 2: Nested map (composite style)
        Map<String, Object> colorableObject = new HashMap<>();
        if (!(value instanceof Map<?, ?>)) {
            return ui -> {};
        }else {
            colorableObject = (Map<String, Object>) value;
        }

        List<Consumer<JComponent>> applyList = new ArrayList<>();

        for (Map.Entry<String, Object> entry : colorableObject.entrySet()) {
            String eKey = entry.getKey();
            Object eObj = entry.getValue();

            switch (eKey) {
                case "bg": {
                    if (eObj instanceof Color) {
                        Consumer<JComponent> consumer = ui -> ui.setBackground((Color) eObj);
                        applyList.add(consumer);
                    }
                    break;
                }
                case "fg": {
                    if (eObj instanceof Color) {
                        Consumer<JComponent> consumer = ui -> ui.setForeground((Color) eObj);
                        applyList.add(consumer);
                    }
                    break;
                }
                case "border": {
                    if (eObj instanceof Map<?, ?>) {
                        Consumer<JComponent> consumer = applyBorder((Map<String, Object>) eObj);
                        applyList.add(consumer);
                    }
                    break;
                }
                case "item": {
                    // Recursively create a sub-applier
                    Consumer<JComponent> child = createApplierFunction(false, (Map<String, Object>) colorableObject, "item", "item", parent);
                    Consumer<JComponent> newParent = (ui) -> {
                        for (Component component : ui.getComponents()) {
                            if (component instanceof JComponent) {
                                child.accept((JComponent) component);
                            }
                        }
                    };
                    applyList.add(newParent);
                    break;
                }
                case "active_textarea": {
                    // Recursively create a sub-applier
                    try {
                        Consumer<JComponent> child = createApplierFunction(false, (Map<String, Object>) colorableObject, "active_textarea", "active_textarea", parent);
                        Color textCursor;

                        if (colorableObject.get("active_textarea") instanceof Map<?, ?>) {
                            if (((Map<String, Object>) colorableObject.get("active_textarea")).get("cursor") instanceof Color) {
                                textCursor = (Color) ((Map<String, Object>) colorableObject.get("active_textarea")).get("cursor");
                            }else {
                                textCursor = Color.BLACK;
                            }
                        }else {
                            textCursor = Color.BLACK;
                        }

                        Consumer<JComponent> newParent = (ui) -> {
                            if (!(ui instanceof TabbedTextArea)) {
                                return;
                            }
                            TextArea area = ((TabbedTextArea) ui).getActiveTextArea();
                            if (area != null) {
                                child.accept(area);
                                area.setCaretColor(textCursor);
                            }
                        };
                        applyList.add(newParent);
                    } catch (Exception e) {
                        System.out.println("Failed to apply active_textarea because of missing or wrong values/keys");
                    }

                    break;
                }
                case "file_tree": {
                    if (eObj instanceof Map<?, ?>) {
                        Consumer<JComponent> consumer = applyFileTreeRenderer((Map<String, Object>) eObj);
                        applyList.add(consumer);
                    }
                    break;
                }
                case "selection": {
                    if (eObj instanceof Map<?, ?>) {
                        Consumer<JComponent> consumer = applySelectionTextComponent((Map<String, Object>) eObj);
                        applyList.add(consumer);
                    }
                    break;
                }
            }
        }

        // Combine all consumers into one
        Consumer<JComponent> combined = ui -> {
            for (Consumer<JComponent> c : applyList) {
                c.accept(ui);
            }
        };

        if (addToList) addToAplierViaKey(savedKey, combined);
        return combined;
    }


    private void addToAplierViaKey(String key, Consumer<JComponent> consumer) {
        List<Consumer<JComponent>> conList = applyFunctions.get(key);
        if (conList == null) {
            conList = new ArrayList<>();
        }
        conList.add(consumer);
        applyFunctions.put(key, conList);
    }

    private Consumer<JComponent> applyScrollBar(Map<String, Object> map) {

    }

    private Consumer<JComponent> applyBorder(Map<String, Object> map) {
        if (map.isEmpty()) {
            return (ui) -> ui.setBorder(BorderFactory.createEmptyBorder());
        }

        Object style = map.get("style");
        try {
            switch ((String) style) {
                case "empty": {
                    return (ui) -> ui.setBorder(BorderFactory.createEmptyBorder());
                }
                case "line": {
                    int thickness = Integer.parseInt(map.get("thickness").toString());
                    Color color = (Color) map.get("color");

                    return (ui) -> ui.setBorder(BorderFactory.createLineBorder(color, thickness));
                }
            }
        }catch (NullPointerException e) {
            return (ui) -> ui.setBorder(BorderFactory.createEmptyBorder());
        }

        return (ui) -> {};
    }

    private Consumer<JComponent> applySelectionTextComponent(Map<String, Object> map) {
        try {
            Color text = (Color) map.get("text");
            Color color = (Color) map.get("color");

            return (ui) -> {
                if (!(ui instanceof JTextComponent)) {
                    return;
                }
                JTextComponent textComponent = (JTextComponent) ui;
                textComponent.setSelectedTextColor(text);
                textComponent.setSelectionColor(color);
            };
        } catch (Exception e) {
            System.out.println("Invalid selection text component");
        }



        return (ui) -> {};
    }

    private Consumer<JComponent> applyFileTreeRenderer(Map<String, Object> map) {
        Consumer<JComponent> selectedComponent = null;
        Consumer<JComponent> nonSelectedComponent = null;
        try {
            Color selectedBg;
            Color selectedFg;
            Color notSelectedFg;
            Color notSelectedBg;


            if (map.get("selected") instanceof Map) {
                Map<String, Object> selectedMap = (Map<String, Object>) map.get("selected");
                selectedFg = (Color) selectedMap.get("fg");
                selectedBg = (Color) selectedMap.get("bg");
            }else {
                selectedFg = Color.BLACK;
                selectedBg = Color.BLACK;
            }

            if (map.get("non_selected") instanceof Map) {
                Map<String, Object> nonSelectedMap = (Map<String, Object>) map.get("non_selected");
                notSelectedFg = (Color) nonSelectedMap.get("fg");
                notSelectedBg = (Color) nonSelectedMap.get("bg");
            } else {
                notSelectedBg = Color.BLACK;
                notSelectedFg = Color.BLACK;
            }
            Consumer<JComponent> consumer = ui -> {
                if (!(ui instanceof FileTree)) {
                    return;
                }
                FileTreeCellRenderer renderer = new FileTreeCellRenderer();
                renderer.setBackgroundSelectionColor(selectedBg);
                renderer.setTextSelectionColor(selectedFg);
                renderer.setBackgroundNonSelectionColor(notSelectedBg);
                renderer.setTextNonSelectionColor(notSelectedFg);
                ((FileTree) ui).setCellRenderer(renderer);
            };
            return consumer;
        }catch (Exception e) {
            System.out.println("Invalid file tree decleration");
        }

        return (ui) -> {};
    }
}

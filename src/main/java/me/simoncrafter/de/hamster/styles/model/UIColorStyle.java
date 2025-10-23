package me.simoncrafter.de.hamster.styles.model;


import com.sun.istack.internal.Nullable;
import me.simoncrafter.de.hamster.editor.view.*;
import me.simoncrafter.de.hamster.editor.view.TextArea;
import me.simoncrafter.de.hamster.styles.controller.StyleSettings;
import me.simoncrafter.de.hamster.styles.controller.UIStyleController;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

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

            if (key.contains("simulation.panel.scroll")) {
                System.out.println("oisfjuölas");
            }

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
                case "scroll": {
                    if (eObj instanceof Map<?, ?>) {
                        Consumer<JComponent> consumer = applyScrollBar((Map<String, Object>) eObj);
                        applyList.add(consumer);
                    }
                    break;
                }
                case "split": {
                    if (eObj instanceof Map<?, ?>) {
                        Consumer<JComponent> consumer = applySplitPlane((Map<String, Object>) eObj);
                        applyList.add(consumer);
                    }
                    break;
                }
                case "opaque": {
                    if (eObj instanceof Boolean) {
                        Consumer<JComponent> consumer = ui -> ui.setOpaque((Boolean) eObj);
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
        Object style = map.get("style");
        try {
            if (!(style instanceof String)) {
                return (ui) -> {};
            }
            Consumer<JComponent> viewPort = (ui) -> {};
            if (map.get("viewport") instanceof Map<?, ?>) {
                viewPort = createApplierFunction(false, map, "viewport", "viewport", null);
            }


            switch ((String) style) {
                case "simple": {
                    Object bg = map.get("bg");
                    Object color = map.get("color");
                    Object inc = map.get("increment");

                    if (!(color instanceof Color && bg instanceof Color && inc instanceof Integer)) {
                        return (ui) -> {};
                    }
                    Consumer<JComponent> finalViewPort = viewPort; // make it final because lamdas are wired
                    return (ui) -> {
                        JScrollPane scrollPane;

                        if (ui instanceof TabbedTextArea) {
                            TabbedTextArea textArea = (TabbedTextArea) ui;
                            if (textArea.getActiveTextArea() == null) {
                                return;
                            }
                            scrollPane = textArea.getActiveScrollPlane();
                        } else if (ui instanceof JScrollPane) {
                            scrollPane = (JScrollPane) ui;
                        } else {
                            return;
                        }
                        finalViewPort.accept(scrollPane.getViewport());

                        JScrollBar vScroll = scrollPane.getVerticalScrollBar();
                        JScrollBar hScroll = scrollPane.getHorizontalScrollBar();
                        scrollPane.setBackground((Color) bg);

                        vScroll.setUI(UIStyleController.getBasicScrollBar((Color) color, 5));
                        vScroll.setUnitIncrement((Integer) inc);

                        hScroll.setUI(UIStyleController.getBasicScrollBar((Color) color, 5));
                        hScroll.setUnitIncrement((Integer) inc);
                    };

                }
            }

        } catch (Exception e) {
            System.out.println("Error while applying Scrollbar style");
        }
        return (ui) -> {};
    }

    private Consumer<JComponent> applySplitPlane(Map<String, Object> map) {
        Object style = map.get("style");
        try {
            if (!(style instanceof String)) {
                System.out.println("Style of split plane has to be string!");
                return (ui) -> {};
            }

            switch ((String) style) {
                case "simple": {
                    Object color = map.get("color");
                    Object hover = map.get("hover");
                    Object handle = map.get("handle");

                    if (!(color instanceof Color && hover instanceof Color && handle instanceof Color)) {
                        System.out.println("Color of split plane has to be color!");
                        return (ui) -> {};
                    }

                    return (ui) -> {
                        if (!(ui instanceof JSplitPane)) {
                            return;
                        }
                        JSplitPane splitPlane = (JSplitPane) ui;
                        splitPlane.setUI(UIStyleController.getSimpleSplitPlane((Color) color, (Color) hover, (Color) handle));
                    };
                }
            }

        }catch (Exception e) {
            System.out.println("Error while loading splitplane style");
        }
        return (ui) -> {};
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

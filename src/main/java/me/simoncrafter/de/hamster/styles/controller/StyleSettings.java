package me.simoncrafter.de.hamster.styles.controller;

import com.kenai.jaffl.struct.Struct;
import com.sun.istack.internal.Nullable;
import me.simoncrafter.de.hamster.styles.model.UIColorStyle;
import me.simoncrafter.de.hamster.workbench.Utils;
import org.python.antlr.ast.Str;
import org.python.antlr.op.In;
import org.yaml.snakeyaml.Yaml;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StyleSettings {
    private static File settingsFile;
    private static String settingsFileName = "settings/colors.yml";

    private static Map<String, Color> presetColors = new HashMap<>();
    private static Map<String, UIColorStyle> colorStyles = new HashMap<>();

    private static final String HEX_COLOR_PATTERN = "^#[A-Fa-f0-9]{6}$";
    private static final String STRING_COLOR_PATTERN = "^(?<red>\\d{1,3}) (?<green>\\d{1,3}) (?<blue>\\d{1,3})$";
    private static final String STRING_NUMBER_COLOR_PATTERN = "^(?<red>\\d{3})(?<green>\\d{3})(?<blue>\\d{3})$";

    public static void init() {
        ensureSettingsFile();
        loadConfig();
    }

    private static void ensureSettingsFile() {
        Utils.ensureSettings();
        settingsFile = Utils.extractResource(settingsFileName, Utils.SETTINGS + Utils.FSEP + "colors.yml");
    }

    private static void loadConfig() {
        try (InputStream input = Files.newInputStream(settingsFile.toPath())) {
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(input);
            Map<String, Object> styles = (Map<String, Object>) data.get("uiStyles");
            Map<String, Object> presets = (Map<String, Object>) data.get("presetColors");

            for (Map.Entry<String, Object> entry : presets.entrySet()) { // read preset colors
                if (!(entry.getValue() instanceof String || entry.getValue() instanceof Integer)) {
                    continue;
                }
                String color = entry.getValue().toString();
                Color createdColor = readColor(false, color);
                if (createdColor == null) {
                    createdColor = Color.RED;
                }
                presetColors.put(entry.getKey(), createdColor);
            }

            for (Map.Entry<String, Object> entry : styles.entrySet()) { // read color styles
                if (entry.getValue() instanceof Map<?, ?>) {
                    UIColorStyle style = readStyle((Map<String, Object>) entry.getValue());
                    if (styles != null) colorStyles.put(entry.getKey(), style);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static @Nullable UIColorStyle readStyle(Map<String, Object> map) {
        if (!(map.get("colors") instanceof Map<?, ?>)) { // early escape
            return null;
        }

        Map<String, Object> colors = (Map<String, Object>) map.get("colors");
        UIColorStyle style = new UIColorStyle(new HashMap<>(), map.get("name").toString());
        for (Map.Entry<String, Object> entry : colors.entrySet()) {
            Color createdColor;

            if (!(entry.getValue() instanceof String || entry.getValue() instanceof Integer)) {
                createdColor = Color.RED;

                continue;
            }
            String color = entry.getValue().toString();
            createdColor = readColor(true, color);
            if (createdColor == null) {
                createdColor = Color.RED;
            }

            System.out.println("Read color: " + createdColor + " with name: " + entry.getKey());
            style.putColor(entry.getKey(), createdColor);
        }
        return style;
    }

    private static @Nullable Color readColor(boolean withPresets, String color) {
        if (Pattern.matches(HEX_COLOR_PATTERN, color)) {
            String red = color.substring(1, 3);
            String green = color.substring(3, 5);
            String blue = color.substring(5, 7);
            return new Color(Integer.parseInt(red, 16), Integer.parseInt(green, 16), Integer.parseInt(blue, 16));
        } else if (Pattern.matches(STRING_COLOR_PATTERN, color)) {
            Pattern pattern = Pattern.compile(STRING_COLOR_PATTERN);
            Matcher matcher = pattern.matcher(color);
            matcher.find();
            String red = matcher.group("red");
            String green = matcher.group("green");
            String blue = matcher.group("blue");
            return new Color(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));

        } else if (Pattern.matches(STRING_NUMBER_COLOR_PATTERN, color)) {
            Pattern pattern = Pattern.compile(STRING_NUMBER_COLOR_PATTERN);
            Matcher matcher = pattern.matcher(color);
            matcher.find();
            String red = matcher.group("red");
            String green = matcher.group("green");
            String blue = matcher.group("blue");
            return new Color(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));

        } else if (color.startsWith("_") && withPresets) {
            return presetColors.get(color.substring(1));
        }else {
            return null;
        }
    }

    public static Map<String, UIColorStyle> getColorStyles() {
        return new HashMap<>(colorStyles);
    }

    public static Map<String, Color> getPresetColors() {
        return new HashMap<>(presetColors);
    }

    private static void reset() {
        presetColors.clear();
        colorStyles.clear();
    }

}

package me.simoncrafter.de.hamster.styles.controller;

import com.kenai.jaffl.struct.Struct;
import com.sun.istack.internal.Nullable;
import me.simoncrafter.de.hamster.styles.model.UIColorStyle;
import me.simoncrafter.de.hamster.styles.model.YamlColorObject;
import me.simoncrafter.de.hamster.workbench.Utils;
import org.python.antlr.ast.Str;
import org.python.antlr.op.In;
import org.yaml.snakeyaml.LoaderOptions;
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
            Yaml yaml = new Yaml(new YamlColorObject(new LoaderOptions()));
            Map<String, Object> data = yaml.load(input);
            Map<String, Object> styles = (Map<String, Object>) data.get("uiStyles");
            Map<String, Object> presets = (Map<String, Object>) data.get("presetColors");

            for (Map.Entry<String, Object> entry : presets.entrySet()) { // read preset colors
                if (!(entry.getValue() instanceof String || entry.getValue() instanceof Integer)) {
                    continue;
                }
                Color createdColor = (Color) entry.getValue();
                if (createdColor == null) {
                    createdColor = Color.RED;
                }
                presetColors.put(entry.getKey(), createdColor);
            }

            for (Map.Entry<String, Object> entry : styles.entrySet()) { // read color styles
                if (entry.getValue() instanceof Map<?, ?>) {
                    UIColorStyle style = readStyle((Map<String, Object>) entry.getValue());
                    System.out.println("Read Style " + style.getName());
                    if (styles != null) colorStyles.put(entry.getKey(), style);
                }
            }


        } catch (Exception e) {
            System.out.println("Error while loading style file!");
            System.out.println("You may have removed \"!color\" from one of the colors");
            e.printStackTrace();
        }
    }

    private static @Nullable UIColorStyle readStyle(Map<String, Object> map) {
        if (!(map.get("colors") instanceof Map<?, ?>)) { // early escape
            return null;
        }

        Map<String, Object> colors = (Map<String, Object>) map.get("colors");
        UIColorStyle style = new UIColorStyle(new HashMap<>(), map.get("name").toString());
        Map<String, Object> color = readColors(colors);
        style.setColors(color);
        return style;
    }

    private static Map<String, Object> readColors(Map<String, Object> input) {

        Map<String, Object> map = new HashMap<>(input);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof Map<?, ?>) {
                map.put(entry.getKey(), (Map<String, Object>) entry.getValue());
            }
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
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

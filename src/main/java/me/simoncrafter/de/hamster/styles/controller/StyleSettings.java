package me.simoncrafter.de.hamster.styles.controller;

import com.kenai.jaffl.struct.Struct;
import com.sun.istack.internal.Nullable;
import jsint.E;
import me.simoncrafter.de.hamster.styles.model.UIColorStyle;
import me.simoncrafter.de.hamster.styles.model.YamlColorObject;
import me.simoncrafter.de.hamster.workbench.Utils;
import org.jruby.RubyProcess;
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
        settingsFile = new File(Utils.SETTINGS + Utils.FSEP + "colors.yml");
        if (!settingsFile.exists()) settingsFile = Utils.extractResource(settingsFileName, Utils.SETTINGS + Utils.FSEP + "colors.yml");
    }

    private static void loadConfig() {
        presetColors = new HashMap<>();
        colorStyles = new HashMap<>();

        try (InputStream input = Files.newInputStream(settingsFile.toPath())) {
            Yaml yaml = new Yaml(new YamlColorObject(new LoaderOptions()));
            Map<String, Object> data = yaml.load(input);

            Map<String, Object> presets = (Map<String, Object>) data.get("presetColors");

            // Phase 1 – load presets normally (simple hex or numbers, no references)
            for (Map.Entry<String, Object> entry : presets.entrySet()) {
                Object v = entry.getValue();
                if (v instanceof Color) {
                    presetColors.put(entry.getKey(), (Color) v);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Finished loading presets");
        System.out.println("Now loading colors");

        // Phase 2 – reparse file (now _red etc. can be resolved)
        try (InputStream input = Files.newInputStream(settingsFile.toPath())) {
            Yaml yaml = new Yaml(new YamlColorObject(new LoaderOptions()));
            Map<String, Object> data = yaml.load(input);

            Map<String, Object> styles = (Map<String, Object>) data.get("uiStyles");
            for (Map.Entry<String, Object> entry : styles.entrySet()) {
                if (entry.getValue() instanceof Map<?, ?> ) {
                    UIColorStyle style = readStyle((Map<String, Object>) entry.getValue());
                    colorStyles.put(entry.getKey(), style);
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
        Map<String, Object> color = readColors(colors);
        style.setColors(color);
        return style;
    }

    private static Map<String, Object> readColors(Map<String, Object> input) {

        Map<String, Object> map = new HashMap<>(input);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof Map<?, ?>) {
                map.put(entry.getKey(), readColors((Map<String, Object>) entry.getValue()));
                continue;
            }
            if (entry.getValue() instanceof Color) {
                map.put(entry.getKey(), (Color) entry.getValue());
                continue;
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

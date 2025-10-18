package me.simoncrafter.de.hamster.styles.controller;

import com.kenai.jaffl.struct.Struct;
import me.simoncrafter.de.hamster.styles.model.UIColorStyle;
import me.simoncrafter.de.hamster.workbench.Utils;
import org.python.antlr.ast.Str;
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
import java.util.regex.Pattern;

public class StyleSettings {
    private static File settingsFile;
    private static String settingsFileName = "settings/colors.yml";

    private static Map<String, Color> presetColors = new HashMap<>();
    private static Map<String, UIColorStyle> colorStyles = new HashMap<>();

    private static final String HEX_COLOR_PATTERN = "^#[A-Fa-f0-9]{6}$";
    private static final String STRING_COLOR_PATTERN = "^(?<red>\\d{3}) (?<green>\\d{3}) (?<blue>\\d{3})$";

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

            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (entry.getValue() instanceof String) {
                    String color = (String) entry.getValue();
                    if (Pattern.matches(HEX_COLOR_PATTERN, color)) {
                        String red = color.substring(0, 1);
                        String green = color.substring(2, 3);
                        String blue = color.substring(4, 5);
                        Color createdColor = new Color(Integer.parseInt(red, 16), Integer.parseInt(green, 16), Integer.parseInt(blue, 16));

                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void reset() {
        presetColors.clear();
        colorStyles.clear();
    }

}

package me.simoncrafter.de.hamster.settings.controler;

import me.simoncrafter.de.hamster.styles.model.YamlColorObject;
import me.simoncrafter.de.hamster.workbench.Utils;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.lang.Object;

public class SettingsLoader {

    private static Yaml yaml = null;

    private static File settingsFile;
    private static String settingsFileName = "settings/settings.yml";

    private static Map<String, Object> data = new HashMap<>();

    private static String selectedStyle = "";
    private final static String SELECTED_STYLE_PATH = "selectedStyle";

    public static void init() {
        ensureSettingsFile();
        load();
    }

    private static void ensureSettingsFile() {
        Utils.ensureSettings();
        settingsFile = new File(Utils.SETTINGS + Utils.FSEP + "settings.yml");
        if (!settingsFile.exists()) settingsFile = Utils.extractResource(settingsFileName, Utils.SETTINGS + Utils.FSEP + "settings.yml");
    }


    public static void load() {
        try (InputStream input = Files.newInputStream(settingsFile.toPath())) {
            yaml = new Yaml(new YamlColorObject(new LoaderOptions()));
            Map<String, Object> data = yaml.load(input);

            Object _selectedStyle = data.get(SELECTED_STYLE_PATH);
            if (_selectedStyle instanceof String) {
                selectedStyle = (String) _selectedStyle;
            }else {
                selectedStyle = "darkMode";
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        ensureSettingsFile();
        try (FileWriter writer = new FileWriter(settingsFile)) {
            yaml.dump(data, writer);
        } catch (Exception e) {
            System.out.println("Unable to save settings file!\n" + e.toString());
        }
    }

    private static void setPath(String path, Object value) {
        setPathRecursive(data, path, value);
    }

    private static void setPathRecursive(Map<String, Object> data, String path, Object value) {
        String[] split = path.split("\\.");
        Object newData = data.get(split[0]);
        if (newData instanceof Map<?,?>) {
            setPathRecursive((Map<String, Object>) newData, path.substring(split[0].length() + 1), value);
            return;
        }else {
            data.put(split[0], value);
        }

    }

    public static String getSelectedStyle() {
        return selectedStyle;
    }

    public static void setSelectedStyle(String selectedStyle) {
        SettingsLoader.selectedStyle = selectedStyle;
        setPath(SELECTED_STYLE_PATH, selectedStyle);
        save();
    }
}

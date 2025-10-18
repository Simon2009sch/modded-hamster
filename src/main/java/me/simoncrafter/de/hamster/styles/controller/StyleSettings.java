package me.simoncrafter.de.hamster.styles.controller;

import me.simoncrafter.de.hamster.workbench.Utils;

import java.io.File;

public class StyleSettings {
    private static File settingsFile;
    private static String settingsFileName = "colors.yml";

    public static void init() {
        ensureSettingsFile();
    }

    private static void ensureSettingsFile() {
        Utils.ensureSettings();
        File file = new File(new File(Utils.SETTINGS), settingsFileName);
        if (!file.exists()) {
            //file.mk();
        }


    }
}

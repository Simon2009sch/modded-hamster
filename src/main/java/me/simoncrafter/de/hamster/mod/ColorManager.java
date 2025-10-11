package me.simoncrafter.de.hamster.mod;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ColorManager {

    private static ColorCollection DEFAULT;
    private static ColorCollection DARKMODE;

    private static ColorCollection current;

    public static void initColors() {
        Map<String, Color> defualtColors = new HashMap<>();
        defualtColors.put("PLAY_AREA_BACKGROUND", new Color(240, 244, 246));
        defualtColors.put("EDITOR_BACKGROUND", new Color(255, 255, 220));
        defualtColors.put("EDITOR_FILE_BACKGROUND", new Color(240, 252, 202));
        defualtColors.put("EDITOR_TEXT_BACKGROUND", new Color(255, 255, 255));
        defualtColors.put("EDITOR_DEFAULT_TEXT_COLOR", new Color(0, 0, 0));
        defualtColors.put("EDITOR_TEXT_PLAYING_BACKGROUND", new Color(255, 255, 255));
        defualtColors.put("EDITOR_LINE_NUMBER_BAR", new Color(55, 55, 55));
        defualtColors.put("EDITOR_TOOL_BAR", new Color(238, 238, 238));
        defualtColors.put("EDITOR_TOOL_BAR_INFO_BOXES", new Color(149, 149, 149));
        defualtColors.put("TEXT_COLOR", new Color(0, 0, 0));
        defualtColors.put("EDITOR_CURSOR_COLOR", new Color(0, 0, 0));
        defualtColors.put("TOP_ROW", new Color(255, 215, 180));
        defualtColors.put("SIMULATION_LOG_BACKGROUND", new Color(255, 255, 255));
        defualtColors.put("SELECTION", new Color(200, 221, 242));
        defualtColors.put("SELECTION_TEXT", new Color(0, 0, 0));
        defualtColors.put("BUTTON_COLOR", new Color(255, 255, 255));
        defualtColors.put("BUTTON_COLOR_ON", new Color(184, 207, 229));
        defualtColors.put("EDITOR_SCROLL_BAR_BACKGROUND", new Color(238, 238, 238));
        defualtColors.put("EDITOR_SCROLL_BAR_BAR", new Color(238, 238, 238));
        defualtColors.put("SLIDER_COLOR", new Color(205, 205, 205));
        defualtColors.put("LOG_PANEL", new Color(255, 255, 255));
        defualtColors.put("LOG_PANEL_BORDER", new Color(228, 228, 228));


        Color dark0 = new Color(25, 25, 25);
        Color dark1 = new Color(40, 40, 40);
        Color dark2 = new Color(55, 55, 55);
        Color dark3 = new Color(70, 70, 70);
        Color dark4 = new Color(85, 85, 85);
        Color dark5 = new Color(100, 100, 100);
        Color dark6 = new Color(115, 115, 115);
        Color dark7 = new Color(130, 130, 130);
        Color dark8 = new Color(145, 145, 145);
        Color dark9 = new Color(160, 160, 160);
        Color dark10 = new Color(175, 175, 175);


        Map<String, Color> darkmodeColors = new HashMap<>();
        darkmodeColors.put("PLAY_AREA_BACKGROUND", dark3);
        darkmodeColors.put("EDITOR_BACKGROUND", dark3);
        darkmodeColors.put("EDITOR_FILE_BACKGROUND", dark5);
        darkmodeColors.put("EDITOR_TEXT_PLAYING_BACKGROUND", new Color(255, 75, 100));
        darkmodeColors.put("EDITOR_DEFAULT_TEXT_COLOR", new Color(255, 100, 150));
        darkmodeColors.put("EDITOR_TOOL_BAR", dark5);
        darkmodeColors.put("EDITOR_TOOL_BAR_INFO_BOXES", dark9);
        darkmodeColors.put("EDITOR_TEXT_BACKGROUND", new Color(10, 200, 255));
        darkmodeColors.put("TEXT_COLOR", Color.WHITE);
        darkmodeColors.put("EDITOR_CURSOR_COLOR", Color.WHITE);
        darkmodeColors.put("EDITOR_LINE_NUMBER_BAR", dark0);
        darkmodeColors.put("TOP_ROW", dark10);
        darkmodeColors.put("SELECTION", new Color(37, 71, 124));
        darkmodeColors.put("SELECTION_TEXT", Color.BLACK);
        darkmodeColors.put("BUTTON_COLOR", dark4);
        darkmodeColors.put("BUTTON_COLOR_ON", dark5);
        darkmodeColors.put("EDITOR_SCROLL_BAR_BACKGROUND", dark2);
        darkmodeColors.put("EDITOR_SCROLL_BAR_BAR", dark3);
        darkmodeColors.put("SLIDER_COLOR",new Color(59, 66, 69));
        darkmodeColors.put("LOG_PANEL", dark5);
        darkmodeColors.put("LOG_PANEL_BORDER", dark3);

        DEFAULT = new ColorCollection(defualtColors);
        DARKMODE = new ColorCollection(darkmodeColors);

        current = DARKMODE;
    }


    public static ColorCollection getCurrent() {
        return current;
    }
}

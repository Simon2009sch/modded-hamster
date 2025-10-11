package me.simoncrafter.de.hamster.mod;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ColorCollection {
    private final Map<String, Color> colors;

    private static final Color DEFAULT_COLOR = Color.WHITE;

    public Color getPlayAreaBackground() {
        return colors.getOrDefault("PLAY_AREA_BACKGROUND", DEFAULT_COLOR);
    }
    public Color getEditorBackground() {
        return colors.getOrDefault("EDITOR_BACKGROUND", DEFAULT_COLOR);
    }
    public Color getEditorFileBackground() {
        return colors.getOrDefault("EDITOR_FILE_BACKGROUND", DEFAULT_COLOR);
    }
    public Color getEditorTextPlayingBackground() {
        return colors.getOrDefault("EDITOR_TEXT_PLAYING_BACKGROUND", DEFAULT_COLOR);
    }
    public Color getEditorTextBackground() {
        return colors.getOrDefault("EDITOR_TEXT_BACKGROUND", DEFAULT_COLOR);
    }
    public Color getTextColor() {
        return colors.getOrDefault("TEXT_COLOR", DEFAULT_COLOR);
    }
    public Color getEditorToolBar() {
        return colors.getOrDefault("EDITOR_TOOL_BAR", DEFAULT_COLOR);
    }
    public Color getEditorCursorColor() {
        return colors.getOrDefault("EDITOR_CURSOR_COLOR", DEFAULT_COLOR);
    }
    public Color getEditorToolBarInfoBoxes() {
        return colors.getOrDefault("EDITOR_TOOL_BAR_INFO_BOXES", DEFAULT_COLOR);
    }
    public Color getEditorDefaultTextColor() {
        return colors.getOrDefault("EDITOR_DEFAULT_TEXT_COLOR", DEFAULT_COLOR);
    }
    public Color getEditorLineNumberBar() {
        return colors.getOrDefault("EDITOR_LINE_NUMBER_BAR", DEFAULT_COLOR);
    }
    public Color getScrollBarBackground() {
        return colors.getOrDefault("SCROLL_BAR_BACKGROUND", DEFAULT_COLOR);
    }
    public Color getScrollBarBar() {
        return colors.getOrDefault("SCROLL_BAR_BAR", DEFAULT_COLOR);
    }
    public Color getTopRow() {
        return colors.getOrDefault("TOP_ROW", DEFAULT_COLOR);
    }
    public Color getSimulationLogBackground() {
        return colors.getOrDefault("SIMULATION_LOG_BACKGROUND", DEFAULT_COLOR);
    }
    public Color getButtonColor() {
        return colors.getOrDefault("BUTTON_COLOR", DEFAULT_COLOR);
    }

    public Color getSelection() {
        return colors.getOrDefault("SELECTION", DEFAULT_COLOR);
    }
    public Color getSelectionText() {
        return colors.getOrDefault("SELECTION_TEXT", DEFAULT_COLOR);
    }
    public Color getSliderColor() {
        return colors.getOrDefault("SLIDER_COLOR", DEFAULT_COLOR);
    }
    public Color getLogPanel() {
        return colors.getOrDefault("LOG_PANEL", DEFAULT_COLOR);
    }
    public Color getLogPanelBorder() {
        return colors.getOrDefault("LOG_PANEL_BORDER", DEFAULT_COLOR);
    }
    public Color get(String color) {
        return colors.getOrDefault(color, DEFAULT_COLOR);
    }


    public ColorCollection(Map<String, Color> colors) {
        if (colors == null) {
            colors = new HashMap<>();
        }
        this.colors = colors;
    }
}

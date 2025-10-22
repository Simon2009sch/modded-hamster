package me.simoncrafter.de.hamster.styles.model;

import com.sun.istack.internal.Nullable;
import me.simoncrafter.de.hamster.styles.controller.StyleSettings;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

import java.awt.*;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YamlColorObject extends Constructor {

    private static final String HEX_COLOR_PATTERN = "^#[A-Fa-f0-9]{6}$";
    private static final String STRING_COLOR_PATTERN = "^(?<red>\\d{1,3}) (?<green>\\d{1,3}) (?<blue>\\d{1,3})$";
    private static final String STRING_NUMBER_COLOR_PATTERN = "^(?<red>\\d{3})(?<green>\\d{3})(?<blue>\\d{3})$";


    public YamlColorObject(LoaderOptions loadingConfig) {
        super(loadingConfig);

        // register handler for !color
        this.yamlConstructors.put(new Tag("!color"), new ConstructColor());
    }

    private class ConstructColor extends AbstractConstruct {
        @Override
        public Object construct(Node node) {
            String value = ((ScalarNode) node).getValue();
            Color color = readColor(value);
            System.out.println("YAML COLOR PARSER: value = " + value + "  Color " + color.getRed() + " " + color.getGreen() + " " + color.getBlue()); // debug
            return color;
        }
    }

    private static @Nullable Color readColor(String color) {
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

        } else if (color.startsWith("_")) {
            Map<String, Color> map = StyleSettings.getPresetColors();
            Color c = map.get(color.substring(1));
            if (c == null) {
                c = Color.BLACK;
            }
            return c;
        }else {
            return null;
        }
    }
}

package ru.itmo.XMLUtils;

import ru.itmo.data.Color;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ColorXMLAdapter extends XmlAdapter<String, Color> {
    @Override
    public Color unmarshal(String s) throws Exception {
        boolean is_valid = false;
        for (Color c : Color.values()) {
            if (c.name().equals(s)) {
                is_valid = true;
            }
        }
        if (!is_valid)
            throw new NumberFormatException("Unknown color value");

        return Color.valueOf(s);
    }

    @Override
    public String marshal(Color color) throws Exception {
        return color.toString();
    }
}

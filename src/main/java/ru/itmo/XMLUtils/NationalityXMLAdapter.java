package ru.itmo.XMLUtils;

import ru.itmo.data.Country;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class NationalityXMLAdapter extends XmlAdapter<String, Country> {
    @Override
    public Country unmarshal(String s) throws Exception {
        boolean is_valid = false;
        for (Country c : Country.values()) {
            if (c.name().equals(s)) {
                is_valid = true;
            }
        }
        if (!is_valid)
            throw new NumberFormatException("Unknown nationality value");
        return Country.valueOf(s);
    }

    @Override
    public String marshal(Country country) throws Exception {
        return country.toString();
    }
}

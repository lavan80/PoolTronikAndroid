package com.pool.tronik.pooltronik.utils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class XmlUtil {
    public static Map<Integer, Integer> parseXml(String sXml) {

        Map<Integer, Integer> map = new HashMap<>();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(sXml));
            int eventType = xpp.getEventType();
            String key = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    key = xpp.getName();
                } else if (eventType == XmlPullParser.TEXT) {
                    if (key.startsWith("relay")) {
                        try {
                            key = key.substring(key.length()-1);
                            //i think 0 is just lamp indicator on the relays box
                            if (!key.equalsIgnoreCase("0")) {
                                map.put(Integer.parseInt(key), Integer.parseInt(xpp.getText()));
                            }
                        } catch (Exception e){}

                    }
                    key = "";
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
        }
        return map;
    }
}

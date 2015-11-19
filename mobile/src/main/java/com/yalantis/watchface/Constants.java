package com.yalantis.watchface;

import java.util.HashMap;
import java.util.Map;

/**
 * @author andrewkhristyan on 10/13/15.
 */
public class Constants {

    public static Map<Integer, String> resourceKeyMap = new HashMap<>();
    public static final int SECOND_CHOOSER = 1;
    public static final int BACKGROUND_CHOOSER = 2;
    public static final int HOUR_CHOOSER = 3;
    public static final int MINUTE_CHOOSER = 4;
    public static final int BACKGROUND_AMBIENT = 5;
    public static final int HOUR_AMBIENT = 6;
    public static final int MINUTE_AMBIENT = 7;

    static {
        resourceKeyMap.put(SECOND_CHOOSER, "tick");
        resourceKeyMap.put(BACKGROUND_CHOOSER, "bg");
        resourceKeyMap.put(HOUR_CHOOSER, "hrs");
        resourceKeyMap.put(MINUTE_CHOOSER, "min");
        resourceKeyMap.put(BACKGROUND_AMBIENT, "bg_ambient");
        resourceKeyMap.put(HOUR_AMBIENT, "hrs_ambient");
        resourceKeyMap.put(MINUTE_AMBIENT, "min_ambient");
    }
}

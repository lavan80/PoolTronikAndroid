package com.pool.tronik.pooltronik.utils;

import com.pool.tronik.pooltronik.comands.RelayOff;
import com.pool.tronik.pooltronik.comands.RelayOn;

import java.util.Arrays;
import java.util.List;

public class RelayConfig {
    public static List<String> RELAY_LIST_ON = Arrays.asList(RelayOn.RELAY1, RelayOn.RELAY2, RelayOn.RELAY3, RelayOn.RELAY4,
            RelayOn.RELAY5, RelayOn.RELAY6, RelayOn.RELAY7, RelayOn.RELAY8);
    public static List<String> RELAY_LIST_OFF = Arrays.asList(RelayOff.RELAY1, RelayOff.RELAY2, RelayOff.RELAY3, RelayOff.RELAY4,
            RelayOff.RELAY5, RelayOff.RELAY6, RelayOff.RELAY7, RelayOff.RELAY8);
    public static final int RELAYS_SIZE = 8;
    public static final int STATUS_ON = 1;
    public static final int STATUS_OFF = 0;
    public static final int STATUS_PENDING = 2;
    public static final int RELAY_1 = 1;
    public static final int RELAY_2 = 2;
    public static final int RELAY_3 = 3;
    public static final int RELAY_4 = 4;
    public static final int RELAY_5 = 5;
    public static final int RELAY_6 = 6;
    public static final int RELAY_7 = 7;
    public static final int RELAY_8 = 8;
}

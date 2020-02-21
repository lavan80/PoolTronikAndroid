package com.pool.tronik.pooltronik.comands;

public enum RelayEnum {
    RELAY1(0,RelayOn.RELAY1,RelayOff.RELAY1),
    RELAY2(0,RelayOn.RELAY2,RelayOff.RELAY2),
    RELAY3(0,RelayOn.RELAY3,RelayOff.RELAY3),
    RELAY4(0,RelayOn.RELAY4,RelayOff.RELAY4),
    RELAY5(0,RelayOn.RELAY5,RelayOff.RELAY5),
    RELAY6(0,RelayOn.RELAY6,RelayOff.RELAY6),
    RELAY7(0,RelayOn.RELAY7,RelayOff.RELAY7),
    RELAY8(0,RelayOn.RELAY8,RelayOff.RELAY8);

    int status;
    String relayOn;
    String relayOff;

    RelayEnum(int status, String relayOn, String relayOff){
        this.status = status;
        this.relayOn = relayOn;
        this.relayOff = relayOff;
    }
}

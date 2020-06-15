package com.pool.tronik.pooltronik.utils;

public class RelayUtil {

    public static void setCorrectState(RelayStatus relayStatus, int relay) {
        if (relayStatus.getStatus() == RelayConfig.STATUS_ON) {
            relayStatus.setRequestedStatus(RelayConfig.STATUS_OFF);
            relayStatus.setCommand(RelayConfig.RELAY_LIST_ON.get(relay));
            relayStatus.setAvailableCommand(RelayConfig.RELAY_LIST_OFF.get(relay));
        }
        else if (relayStatus.getStatus() == RelayConfig.STATUS_OFF) {
            relayStatus.setRequestedStatus(RelayConfig.STATUS_ON);
            relayStatus.setCommand(RelayConfig.RELAY_LIST_ON.get(relay));
            relayStatus.setAvailableCommand(RelayConfig.RELAY_LIST_ON.get(relay));
        }
    }
}

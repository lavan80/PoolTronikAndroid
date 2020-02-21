package com.pool.tronik.pooltronik.utils;

import java.io.Serializable;

public class RelayStatus implements Serializable {

    private int relay;
    private String command;
    private String availableCommand;
    private int status, requestedStatus;
    private String name;

    public int getRelay() {
        return relay;
    }

    public void setRelay(int relay) {
        this.relay = relay;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAvailableCommand() {
        return availableCommand;
    }

    public void setAvailableCommand(String availableCommand) {
        this.availableCommand = availableCommand;
    }

    public int getRequestedStatus() {
        return requestedStatus;
    }

    public void setRequestedStatus(int requestedStatus) {
        this.requestedStatus = requestedStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

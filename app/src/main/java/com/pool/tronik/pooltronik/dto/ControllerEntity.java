package com.pool.tronik.pooltronik.dto;

import java.io.Serializable;

public class ControllerEntity implements Serializable {

    private long id;
    private String controllerIp;
    private int controllerType = ControllerType.WEB_RELAY.ordinal();//currently the switcher only
    private int controllerKind = ControllerKind.POOL.ordinal();//what is connected in

    public String getControllerIp() {
        return controllerIp;
    }

    public void setControllerIp(String controllerIp) {
        this.controllerIp = controllerIp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getControllerType() {
        return controllerType;
    }

    public void setControllerType(int controllerType) {
        this.controllerType = controllerType;
    }

    public int getControllerKind() {
        return controllerKind;
    }

    public void setControllerKind(int controllerKind) {
        this.controllerKind = controllerKind;
    }
}

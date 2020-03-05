package com.pool.tronik.pooltronik.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by andreivasilevitsky on 16/06/2019.
 */
public class PTScheduleDate implements Serializable{
    private int id;
    private int relay;
    private int status;// off, on, remove
    private String startDate;
    private List<String> nextDates;
    private int duration = 0;
    private int iteration;
    private List<Integer> repeatList;

    public PTScheduleDate() {
        repeatList = new ArrayList<>();
        nextDates = new ArrayList<>();
    }

    public int getRelay() {
        return relay;
    }

    public void setRelay(int relay) {
        this.relay = relay;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public List<String> getNextDates() {
        return nextDates;
    }

    public void setNextDates(List<String> nextDates) {
        this.nextDates = nextDates;
    }

    public void addNextDate(String localDateTime){
        nextDates.add(localDateTime);
    }

    public void removeNextDate(String localDateTime) {
        int index = nextDates.indexOf(localDateTime);
        if (index >= 0)
            nextDates.remove(index);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public List<Integer> getRepeatList() {
        return repeatList;
    }

    public void setRepeatList(List<Integer> repeatList) {
        this.repeatList = repeatList;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PTScheduleDate that = (PTScheduleDate) o;
        return relay == that.relay &&
                status == that.status &&
                duration == that.duration &&
                iteration == that.iteration &&
                startDate.equals(that.startDate) &&
                id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(relay, status, startDate, duration, iteration, id);
    }
}

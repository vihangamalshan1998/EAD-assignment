package com.example.fuel_management_mobile_app.model;

public class Id {
    private Integer timestamp;
    private Integer machine;
    private Integer pid;
    private Integer increment;
    private String creationTime;

    public Id(Integer timestamp, Integer machine, Integer pid, Integer increment, String creationTime) {
        this.timestamp = timestamp;
        this.machine = machine;
        this.pid = pid;
        this.increment = increment;
        this.creationTime = creationTime;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getMachine() {
        return machine;
    }

    public void setMachine(Integer machine) {
        this.machine = machine;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getIncrement() {
        return increment;
    }

    public void setIncrement(Integer increment) {
        this.increment = increment;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public String toString() {
        return "Id{" +
                "timestamp=" + timestamp +
                ", machine=" + machine +
                ", pid=" + pid +
                ", increment=" + increment +
                ", creationTime='" + creationTime + '\'' +
                '}';
    }
}

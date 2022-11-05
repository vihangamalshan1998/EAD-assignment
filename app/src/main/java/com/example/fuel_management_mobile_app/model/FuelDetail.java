package com.example.fuel_management_mobile_app.model;

public class FuelDetail {

    private int capacity;
    private int totalAvailable;
    private int inQueueCount;
    private int Petrol;

    public FuelDetail(int capacity, int total_Available, int in_Queue_count, int petrol) {
        this.capacity = capacity;
        totalAvailable = total_Available;
        inQueueCount = in_Queue_count;
        Petrol = petrol;
    }
    public FuelDetail(){

    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getTotalAvailable() {
        return totalAvailable;
    }

    public void setTotalAvailable(int totalAvailable) {
        this.totalAvailable = totalAvailable;
    }

    public int getInQueueCount() {
        return inQueueCount;
    }

    public void setInQueueCount(int inQueueCount) {
        this.inQueueCount = inQueueCount;
    }

    public int getPetrol() {
        return Petrol;
    }

    public void setPetrol(int petrol) {
        Petrol = petrol;
    }

    @Override
    public String toString() {
        return "FuelDetail{" +
                "Capacity=" + capacity +
                ", Total_Available=" + totalAvailable +
                ", In_Queue_count=" + inQueueCount +
                ", Petrol=" + Petrol +
                '}';
    }
}

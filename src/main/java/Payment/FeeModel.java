package Payment;


import Vehicles.VehicleType;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class FeeModel {

    private int start;
    private int end;
    private int price;
    private VehicleType vehicleType;

    public FeeModel(int start, int end, int price, VehicleType vehicleType) {
        this.start = start;
        this.end = end;
        this.price = price;
        this.vehicleType = vehicleType;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }


}

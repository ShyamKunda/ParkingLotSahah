package Payment;

import Parking.ParkingSpot;

import java.time.LocalDateTime;

public class ParkingTicket {

    private String id;

    private int vehicleID;
    private ParkingSpot parkingSpot;


    private LocalDateTime parkingTime;


    public ParkingTicket(int vehicleID, ParkingSpot parkingSpot, LocalDateTime parkingTime) {
        this.id = Integer.toString(parkingSpot.getParkingSpotID()) +
                Integer.toString(parkingSpot.getParkingLevelID()) + "-" + parkingSpot.getParkingSpotType();
        this.vehicleID = vehicleID;
        this.parkingSpot = parkingSpot;
        this.parkingTime = parkingTime;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public LocalDateTime getParkingTime() {
        return parkingTime;
    }


    @Override
    public String toString() {
        return "===============\nParkingTicket\n{" +
                "\nid='" + id + '\'' +
                ", \nvehicleID=" + vehicleID +
                ", \nparkingSpot=" + parkingSpot +
                ", \nparkingTime=" + parkingTime +
                "\n}\n===============\n";
    }

    public void setParkingTime(LocalDateTime parkingTime) {
        this.parkingTime = parkingTime;
    }
}

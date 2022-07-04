package Payment;

import Parking.ParkingSpot;

import java.time.LocalDateTime;

public class ParkingTicket {


    private int vehicleID;
    private ParkingSpot parkingSpot;


    private LocalDateTime parkingTime;


    public ParkingTicket(int vehicleID, ParkingSpot parkingSpot, LocalDateTime parkingTime) {
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
        return "Parking ticket:\n" +
                "vehicleID=" + vehicleID + "\n" +
                "parkingSpot=" + parkingSpot.getParkingSpotID() + " on level " + parkingSpot.getParkingLevelID()+ "\n" +
                "parkingTime=" + parkingTime + "\n";
    }

    public void setParkingTime(LocalDateTime parkingTime) {
        this.parkingTime = parkingTime;
    }
}

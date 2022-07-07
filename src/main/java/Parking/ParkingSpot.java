package Parking;

public class ParkingSpot {

    private String id;
    private int parkingSpotID;
    private int parkingLevelID;
    private ParkingSpotType parkingSpotType;

    @Override
    public String toString() {
        return "ParkingSpot\n{" +
                "\n id='" + id + '\'' +
                ",\n parkingSpotID=" + parkingSpotID +
                ",\n parkingLevelID=" + parkingLevelID +
                ",\n parkingSpotType=" + parkingSpotType +
                "\n}";
    }

    public ParkingSpot(int parkingSpotID, int parkingLevelID, ParkingSpotType parkingSpotType) {
        this.parkingSpotID = parkingSpotID;
        this.parkingLevelID = parkingLevelID;
        this.parkingSpotType = parkingSpotType;
        this.id = Integer.toString(parkingSpotID) +
                Integer.toString(parkingLevelID) + "-" + parkingSpotType;
    }

    public int getParkingSpotID() {
        return parkingSpotID;
    }

    public int getParkingLevelID() {
        return parkingLevelID;
    }

    public ParkingSpotType getParkingSpotType() {
        return parkingSpotType;
    }
}

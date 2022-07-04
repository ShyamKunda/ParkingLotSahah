package Parking.Factories;

import Parking.*;
import Vehicles.VehicleType;

import java.util.*;

public class ParkingLevelsCollectionFactory {
    private int NUM_OF_LEVELS ;
    private int NUM_OF_SPOT_TYPES;
    private int NUM_OF_SPOTS_PER_SPOT_TYPE ;

    public ParkingLevelsCollectionFactory(int NUM_OF_LEVELS, int NUM_OF_SPOT_TYPES, int NUM_OF_SPOTS_PER_SPOT_TYPE) {
        this.NUM_OF_LEVELS = NUM_OF_LEVELS;
        this.NUM_OF_SPOT_TYPES = NUM_OF_SPOT_TYPES;
        this.NUM_OF_SPOTS_PER_SPOT_TYPE = NUM_OF_SPOTS_PER_SPOT_TYPE;
    }

    public int getNUM_OF_LEVELS() {
        return NUM_OF_LEVELS;
    }

    public void setNUM_OF_LEVELS(int NUM_OF_LEVELS) {
        this.NUM_OF_LEVELS = NUM_OF_LEVELS;
    }

    public int getNUM_OF_SPOT_TYPES() {
        return NUM_OF_SPOT_TYPES;
    }

    public void setNUM_OF_SPOT_TYPES(int NUM_OF_SPOT_TYPES) {
        this.NUM_OF_SPOT_TYPES = NUM_OF_SPOT_TYPES;
    }

    public int getNUM_OF_SPOTS_PER_SPOT_TYPE() {
        return NUM_OF_SPOTS_PER_SPOT_TYPE;
    }

    public void setNUM_OF_SPOTS_PER_SPOT_TYPE(int NUM_OF_SPOTS_PER_SPOT_TYPE) {
        this.NUM_OF_SPOTS_PER_SPOT_TYPE = NUM_OF_SPOTS_PER_SPOT_TYPE;
    }

    public  ParkingLevelsCollection createParkingLevelsCollection() {
        ParkingLevelsCollection levels =
                new ArrayListParkingLevelsCollection();
        int parkingSpotID = 0;
        int parkingLevelID = 0;
        for (int i = 0; i < getNUM_OF_LEVELS(); i++) {
            Map<ParkingSpotType, Collection<ParkingSpot>> allSpotsForLevel = generateSpots(parkingLevelID, parkingSpotID);

            VehicleToParkingSpotTypeMapper vehicleToSpotTypesMapper = typesMapper();
            ParkingLevel parkingLevel = new CParkingLevel(parkingLevelID, allSpotsForLevel,
                    vehicleToSpotTypesMapper);
            levels.add(parkingLevel);
            parkingSpotID += NUM_OF_SPOT_TYPES * NUM_OF_SPOTS_PER_SPOT_TYPE;
            parkingLevelID++;
        }
        return levels;
    }

    private static VehicleToParkingSpotTypeMapper typesMapper() {
        Map<VehicleType, Collection<ParkingSpotType>> typesMapper =
                new HashMap<>();
        typesMapper.put(VehicleType.MOTORCYCLE,
                Arrays.asList(ParkingSpotType.MOTORCYCLE,
                        ParkingSpotType.COMPACT,
                        ParkingSpotType.LARGE));
        typesMapper.put(VehicleType.CAR,
                Arrays.asList(ParkingSpotType.COMPACT, ParkingSpotType.LARGE));
        typesMapper.put(VehicleType.TRUCK,
                Collections.singletonList(ParkingSpotType.LARGE));
        return typesMapper::get;
    }

    private  Map<ParkingSpotType, Collection<ParkingSpot>> generateSpots(int parkingLevelID, int parkingSpotID) {
        Map<ParkingSpotType, Collection<ParkingSpot>> allSpotsForLevel = new HashMap<>();
        for (ParkingSpotType parkingSpotType : ParkingSpotType.values()) {
            Collection<ParkingSpot> spotsForLevel = new ArrayList<>();
            for (int i = 0; i < getNUM_OF_SPOTS_PER_SPOT_TYPE(); i++) {
                spotsForLevel.add(new ParkingSpot(parkingSpotID,
                        parkingLevelID, parkingSpotType));
                parkingSpotID++;
            }
            allSpotsForLevel.put(parkingSpotType, spotsForLevel);
        }
        return allSpotsForLevel;
    }
}

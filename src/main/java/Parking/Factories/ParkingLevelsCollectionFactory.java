package Parking.Factories;

import Parking.*;
import Vehicles.VehicleType;

import java.util.*;

public class ParkingLevelsCollectionFactory {
    private int NUM_OF_LEVELS ;
    private int NUM_OF_SPOT_TYPES;
    private int NUM_OF_SPOTS_PER_SPOT_TYPE ;

    private Map<ParkingSpotType, Integer> NUM_OF_SPOTS_PER_SPOT_TYPE_Map ;

    public ParkingLevelsCollectionFactory(int NUM_OF_LEVELS, int NUM_OF_SPOT_TYPES, int NUM_OF_SPOTS_PER_SPOT_TYPE) {
        this.NUM_OF_LEVELS = NUM_OF_LEVELS;
        this.NUM_OF_SPOT_TYPES = NUM_OF_SPOT_TYPES;
        this.NUM_OF_SPOTS_PER_SPOT_TYPE = NUM_OF_SPOTS_PER_SPOT_TYPE;
    }

    public ParkingLevelsCollectionFactory(int NUM_OF_LEVELS, int NUM_OF_SPOT_TYPES, Map<ParkingSpotType, Integer> NUM_OF_SPOTS_PER_SPOT_TYPE_Map) {
        this.NUM_OF_LEVELS = NUM_OF_LEVELS;
        this.NUM_OF_SPOT_TYPES = NUM_OF_SPOT_TYPES;
        this.NUM_OF_SPOTS_PER_SPOT_TYPE_Map = NUM_OF_SPOTS_PER_SPOT_TYPE_Map;
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

    public int getNUM_OF_SPOTS_PER_SPOT_TYPE(Map<ParkingSpotType, Integer> spotAllocations, ParkingSpotType parkingSpotType) {
        return spotAllocations.get(parkingSpotType);
    }

    public void setNUM_OF_SPOTS_PER_SPOT_TYPE(int NUM_OF_SPOTS_PER_SPOT_TYPE) {
        this.NUM_OF_SPOTS_PER_SPOT_TYPE = NUM_OF_SPOTS_PER_SPOT_TYPE;
    }


    public  ParkingLevelsCollection createParkingLevelsCollection() {
        ParkingLevelsCollection levels =
                new ArrayListParkingLevelsCollection();
        int parkingSpotID = 0;
        int parkingLevelID = 0;
        Map<ParkingSpotType, Collection<ParkingSpot>> allSpotsForLevel;
        for (int i = 0; i < getNUM_OF_LEVELS(); i++) {
            if (i < getNUM_OF_LEVELS()-1) {
                allSpotsForLevel = generateSpots(parkingLevelID, parkingSpotID, NUM_OF_SPOTS_PER_SPOT_TYPE_Map,getNUM_OF_LEVELS() );
            } else {
                Map<ParkingSpotType, Integer> remainingSpots = new HashMap<>();
                for (ParkingSpotType parkingSpotType : ParkingSpotType.values()) {
                    int totalVacantSpots = 0;
                    for(ParkingLevel level : levels ){
                        totalVacantSpots += level.getNumOfVacantSpotsOfType(parkingSpotType);
                    }
                    remainingSpots.put(parkingSpotType, NUM_OF_SPOTS_PER_SPOT_TYPE_Map.get(parkingSpotType) - totalVacantSpots);
                }
                allSpotsForLevel = generateSpotsLastLevel(parkingLevelID, parkingSpotID, remainingSpots,getNUM_OF_LEVELS() );
            }


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
                Arrays.asList(ParkingSpotType.MOTORCYCLE));
        typesMapper.put(VehicleType.CAR,
                Arrays.asList(ParkingSpotType.COMPACT));
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

    private  Map<ParkingSpotType, Collection<ParkingSpot>> generateSpots(int parkingLevelID, int parkingSpotID, Map<ParkingSpotType, Integer> spotAllocations, int totalLevels) {
        Map<ParkingSpotType, Collection<ParkingSpot>> allSpotsForLevel = new HashMap<>();
        for (ParkingSpotType parkingSpotType : ParkingSpotType.values()) {
            Collection<ParkingSpot> spotsForLevel = new ArrayList<>();
            int totalSpotForEachTypeInEachLevel;
            totalSpotForEachTypeInEachLevel = spotAllocations.get(parkingSpotType) / totalLevels;
            for (int i = 0; i < totalSpotForEachTypeInEachLevel; i++) {
                spotsForLevel.add(new ParkingSpot(parkingSpotID,
                        parkingLevelID, parkingSpotType));
                parkingSpotID++;
            }
            allSpotsForLevel.put(parkingSpotType, spotsForLevel);
        }
        return allSpotsForLevel;
    }

    private  Map<ParkingSpotType, Collection<ParkingSpot>> generateSpotsLastLevel(int parkingLevelID, int parkingSpotID, Map<ParkingSpotType, Integer> remainingSpots, int totalLevels) {
        Map<ParkingSpotType, Collection<ParkingSpot>> allSpotsForLevel = new HashMap<>();
        for (ParkingSpotType parkingSpotType : ParkingSpotType.values()) {
            Collection<ParkingSpot> spotsForLevel = new ArrayList<>();
            int totalSpotForEachTypeInEachLevel;
            totalSpotForEachTypeInEachLevel = remainingSpots.get(parkingSpotType);
            for (int i = 0; i < totalSpotForEachTypeInEachLevel; i++) {
                spotsForLevel.add(new ParkingSpot(parkingSpotID,
                        parkingLevelID, parkingSpotType));
                parkingSpotID++;
            }
            allSpotsForLevel.put(parkingSpotType, spotsForLevel);
        }
        return allSpotsForLevel;
    }

}

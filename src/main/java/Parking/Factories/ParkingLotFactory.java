package Parking.Factories;

import Parking.ParkingLevelsCollection;
import Parking.ParkingLot;
import Parking.ParkingSpotType;
import Parking.Policies.BasicLevelAssignmentPolicy;
import Parking.Policies.BasicParkingAssignmentPolicy;
import Parking.Policies.LevelAssignmentPolicy;
import Parking.Policies.ParkingAssignmentPolicy;
import Payment.FeeModel;

import java.util.List;
import java.util.Map;

public class ParkingLotFactory {

    public ParkingLot createNewParkingLot(int numOfLevels, int parkingSpotTypes, Map<ParkingSpotType, Integer> noOfSpotsPerSpotType, List<FeeModel> feeModelList) {
        ParkingLevelsCollectionFactory parkingLevelsCollectionFactory = new ParkingLevelsCollectionFactory(numOfLevels, parkingSpotTypes, noOfSpotsPerSpotType);
        ParkingLevelsCollection parkingLevels = parkingLevelsCollectionFactory.createParkingLevelsCollection();
        LevelAssignmentPolicy levelAssignmentPolicy = new BasicLevelAssignmentPolicy();
        ParkingAssignmentPolicy parkingAssignmentPolicy =
                new BasicParkingAssignmentPolicy();
        return new ParkingLot(parkingLevels, levelAssignmentPolicy,
                parkingAssignmentPolicy, feeModelList);
    }
}

package Parking.Policies;

import Parking.ParkingLevel;
import Parking.ParkingSpot;
import Parking.ParkingSpotType;
import Vehicles.Vehicle;
import Vehicles.VehicleType;

import java.util.Iterator;

public class BasicLevelAssignmentPolicy implements LevelAssignmentPolicy{
    @Override
    public ParkingLevel assignLevel(Iterable<ParkingLevel> levels, Vehicle vehicle) {


        ParkingSpotType parkingSpotType = ParkingSpotType.values()[VehicleType.valueOf(vehicle.getVehicleType().name()).ordinal()];
        ParkingLevel level = null;

           Iterator<ParkingLevel> parkingLevelIterator = levels.iterator();
            for (Iterator<ParkingLevel> it = parkingLevelIterator; it.hasNext(); ) {
                ParkingLevel parkingLevel = it.next();
                if (parkingLevel.getNumOfVacantSpotsOfType(parkingSpotType) >= 1) {
                    level = parkingLevel;
                    break;
                }
            }
            return  level;
    }

}

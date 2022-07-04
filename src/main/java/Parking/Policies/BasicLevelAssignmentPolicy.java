package Parking.Policies;

import Parking.ParkingLevel;
import Vehicles.Vehicle;

public class BasicLevelAssignmentPolicy implements LevelAssignmentPolicy{
    @Override
    public ParkingLevel assignLevel(Iterable<ParkingLevel> levels,
                                    Vehicle vehicle) {
        return levels.iterator().next();
    }
}

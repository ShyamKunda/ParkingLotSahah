package Parking.Policies;

import Exceptions.ParkingLotIsFullException;
import Parking.ParkingSpot;
import Parking.ParkingSpotType;

import java.util.Collection;
import java.util.Map;

public interface ParkingAssignmentPolicy {
    ParkingSpot assignSpot(Map<ParkingSpotType, ? extends Iterable<ParkingSpot>> vacantSpots,
                           Collection<ParkingSpotType> possibleParkingSpotTypes) throws ParkingLotIsFullException;
}

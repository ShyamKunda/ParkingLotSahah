package Parking.Policies;

import Exceptions.ParkingLotIsFullException;
import Parking.ParkingSpot;
import Parking.ParkingSpotType;

import java.util.Collection;
import java.util.Map;

public class BasicParkingAssignmentPolicy implements ParkingAssignmentPolicy {
    @Override
    public ParkingSpot assignSpot(Map<ParkingSpotType,
            ? extends Iterable<ParkingSpot>> vacantSpots,
                                  Collection<ParkingSpotType> possibleParkingSpotTypes) throws ParkingLotIsFullException {
        ParkingSpotType parkingSpotType =
                possibleParkingSpotTypes.iterator().next();
        if (vacantSpots.get(parkingSpotType).iterator().hasNext()) {
            return vacantSpots.get(parkingSpotType).iterator().next();
        } else {
            System.out.println("No vacante spot available");
            throw new ParkingLotIsFullException("No vacant spots");
        }

    }
}

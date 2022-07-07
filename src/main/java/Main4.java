import DisplayBoard.ConsoleDisplayBoard;
import DisplayBoard.IDisplayBoard;
import Exceptions.InvalidParkingLevelException;
import Parking.Factories.ParkingLotFactory;
import Parking.ParkingLot;
import Parking.ParkingSpotType;
import Payment.FeeModel;
import Payment.PaymentCriteria;
import Payment.PaymentTicket;
import Users.VehicleOwner;
import Vehicles.Vehicle;
import Vehicles.VehicleType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main4 {

    public static void main(String[] args) throws InvalidParkingLevelException {

        List<FeeModel> airportFeeModel = new ArrayList<>();
        FeeModel.Builder feeModelBuilder = FeeModel.Builder.newInstance();
        airportFeeModel.add(feeModelBuilder.setStart(0).setEnd(1).setPrice(0).setVehicleType(VehicleType.MOTORCYCLE)
                .setPaymentCriteria(PaymentCriteria.HOURS).build());
        airportFeeModel.add(feeModelBuilder.setStart(1).setEnd(7).setPrice(40).setVehicleType(VehicleType.MOTORCYCLE)
                .setPaymentCriteria(PaymentCriteria.HOURS).build());
        airportFeeModel.add(new FeeModel(1,7,40, VehicleType.MOTORCYCLE, PaymentCriteria.HOURS));
        airportFeeModel.add(new FeeModel(8,24,60, VehicleType.MOTORCYCLE, PaymentCriteria.HOURS));
        airportFeeModel.add(new FeeModel(24,-1,80, VehicleType.MOTORCYCLE, PaymentCriteria.DAYS));
        airportFeeModel.add(new FeeModel(0,11,60, VehicleType.CAR, PaymentCriteria.HOURS));
        airportFeeModel.add(new FeeModel(12,24,80, VehicleType.CAR, PaymentCriteria.HOURS));
        airportFeeModel.add(new FeeModel(24,-1,100, VehicleType.CAR, PaymentCriteria.DAYS, true, true));
        Map<ParkingSpotType, Integer> spotAllocations = new HashMap<>();
        spotAllocations.put(ParkingSpotType.MOTORCYCLE, 0);
        spotAllocations.put(ParkingSpotType.CAR, 1);
        spotAllocations.put(ParkingSpotType.TRUCK, 0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
        ParkingLotFactory parkingLotFactory = new ParkingLotFactory();
        IDisplayBoard displayBoard=new ConsoleDisplayBoard();
        ParkingLot parkingLot = parkingLotFactory.createNewParkingLot(1, ParkingSpotType.values().length, spotAllocations, airportFeeModel);
        System.out.println("Parking Lot created");
        Vehicle vehicle1 = new Vehicle(1, VehicleType.CAR);
        VehicleOwner carOwner1 = new VehicleOwner("Chinnu", vehicle1);
        carOwner1.parkVehicle(parkingLot, LocalDateTime.parse("29-May-2022 14:04:07", formatter));
        System.out.println("Vehicle 1 parked");
        displayBoard.displayNumOfVacantSpotsForEachLevel(parkingLot.getAllLevels());
        String unParkingTime = "01-Jun-2022 14:04:07";
        LocalDateTime unParkingTimeLocal = LocalDateTime.parse(unParkingTime, formatter);
        PaymentTicket paymentTicket =  carOwner1.unparkVehicleAndGetAmount(parkingLot, unParkingTimeLocal);
        System.out.println("------");
        System.out.println(paymentTicket);
        System.out.println("------");
        displayBoard.displayNumOfVacantSpotsForEachLevel(parkingLot.getAllLevels());
    }
}

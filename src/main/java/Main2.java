import DisplayBoard.ConsoleDisplayBoard;
import DisplayBoard.IDisplayBoard;
import Parking.Factories.ParkingLotFactory;
import Parking.ParkingLot;
import Parking.ParkingSpotType;
import Payment.FeeModel;
import Payment.PaymentTicket;
import Users.CarOwner;
import Vehicles.Vehicle;
import Vehicles.VehicleType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Main2 {

    public static void main(String[] args) {

        List<FeeModel> feeModelList = new ArrayList<>();
        feeModelList.add(new FeeModel(0,3,40, VehicleType.MOTORCYCLE));
        feeModelList.add(new FeeModel(4,11,50, VehicleType.MOTORCYCLE));
        feeModelList.add(new FeeModel(12,18,60, VehicleType.MOTORCYCLE));
        feeModelList.add(new FeeModel(19,24,1000, VehicleType.MOTORCYCLE));
        feeModelList.add(new FeeModel(25,-1,1, VehicleType.MOTORCYCLE));
        feeModelList.add(new FeeModel(0,3,70, VehicleType.CAR));
        feeModelList.add(new FeeModel(4,11,120, VehicleType.CAR));
        feeModelList.add(new FeeModel(12,-1,200, VehicleType.CAR));


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
        ParkingLotFactory parkingLotFactory = new ParkingLotFactory();
        IDisplayBoard displayBoard=new ConsoleDisplayBoard();
        ParkingLot parkingLot = parkingLotFactory.createNewParkingLot(1, ParkingSpotType.values().length, 1, feeModelList);
        System.out.println("Parking Lot created");
        Vehicle vehicle1 = new Vehicle(1, VehicleType.CAR);
        CarOwner carOwner1 = new CarOwner("Chinnu", vehicle1);
        carOwner1.parkVehicle(parkingLot, LocalDateTime.parse("29-May-2022 14:04:07", formatter));
        System.out.println("Vehicle 1 parked");
        displayBoard.displayNumOfVacantSpotsForEachLevel(parkingLot.getAllLevels());
        String unParkingTime = "29-May-2022 16:04:07";
        LocalDateTime unParkingTimeLocal = LocalDateTime.parse(unParkingTime, formatter);
        PaymentTicket paymentTicket =  carOwner1.unparkVehicleAndGetAmount(parkingLot, unParkingTimeLocal);
        System.out.println("------");
        System.out.println(paymentTicket);
        System.out.println("------");
        displayBoard.displayNumOfVacantSpotsForEachLevel(parkingLot.getAllLevels());



    }
}

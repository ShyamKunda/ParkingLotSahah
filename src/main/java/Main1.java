//import DisplayBoard.ConsoleDisplayBoard;
//import DisplayBoard.IDisplayBoard;
//import Parking.Factories.ParkingLotFactory;
//import Parking.ParkingLot;
//import Parking.ParkingSpotType;
//import Payment.CashPayment;
//import Payment.FeeModel;
//import Payment.ParkingTicket;
//import Payment.PaymentTicket;
//import Users.CarOwner;
//import Vehicles.Vehicle;
//import Vehicles.VehicleType;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.TreeMap;
//import java.util.stream.Collectors;
//
//
//public class Main1 {
//
//    public static int getAmount(List<FeeModel> feeModelList, int hours, VehicleType vehicleType) {
//        TreeMap<Integer, FeeModel> specificVehichleFeeModelTreeMap = new TreeMap<>();
//        List<FeeModel> specificVehicleFeeModelTreeMap = feeModelList.stream().filter(feeModel -> feeModel.getVehicleType() == vehicleType).collect(Collectors.toList());
//
//        specificVehicleFeeModelTreeMap.forEach(feeModel -> specificVehichleFeeModelTreeMap.put(feeModel.getStart(), feeModel));
//        FeeModel finalFeeModel = specificVehichleFeeModelTreeMap.floorEntry(hours).getValue();
//        int intervalStart = finalFeeModel.getStart();
//        System.out.println("Total Hours: " + hours);
//        System.out.println("Interval start: " + finalFeeModel.getStart() + " Price: " + finalFeeModel.getPrice());
//        int totalCost = 0;
//        if (finalFeeModel.getStart() == specificVehichleFeeModelTreeMap.lastKey()) {
//            System.out.println("This is last key");
//            for (FeeModel feeModel : specificVehicleFeeModelTreeMap) {
//                totalCost+= feeModel.getPrice();
//                if (feeModel.getStart() == intervalStart) {
//                    break;
//                }
//            }
//            totalCost = totalCost - specificVehichleFeeModelTreeMap.lastEntry().getValue().getPrice();
//            System.out.println("Previous cost computations done " + totalCost );
//            int remainingHours = hours - specificVehichleFeeModelTreeMap.lastKey();
//            System.out.println("Remaining hours: " +  remainingHours);
//            int costForRemainingHours = remainingHours* specificVehichleFeeModelTreeMap.lastEntry().getValue().getPrice();
//            totalCost = totalCost + costForRemainingHours;
//            System.out.println(totalCost);
//            System.out.println("Total cost " + totalCost);
//        } else {
//            System.out.println("This is not last key");
//            for (FeeModel feeModel : specificVehicleFeeModelTreeMap) {
//                totalCost+= feeModel.getPrice();
//                if (feeModel.getStart() == intervalStart) {
//                    break;
//                }
//            }
//            System.out.println("Total cost " + totalCost);
//        }
//        return totalCost;
//    }
//
//
//    public static void main(String[] args) {
//
//        List<FeeModel> feeModelList = new ArrayList<>();
//        feeModelList.add(new FeeModel(0,3,40, VehicleType.MOTORCYCLE));
//        feeModelList.add(new FeeModel(4,11,50, VehicleType.MOTORCYCLE));
//        feeModelList.add(new FeeModel(12,18,60, VehicleType.MOTORCYCLE));
//        feeModelList.add(new FeeModel(19,24,1000, VehicleType.MOTORCYCLE));
//        feeModelList.add(new FeeModel(25,-1,1, VehicleType.MOTORCYCLE));
//        feeModelList.add(new FeeModel(0,3,70, VehicleType.CAR));
//        feeModelList.add(new FeeModel(4,11,120, VehicleType.CAR));
//        feeModelList.add(new FeeModel(12,-1,200, VehicleType.CAR));
//
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
//        ParkingLotFactory parkingLotFactory = new ParkingLotFactory();
//        IDisplayBoard displayBoard=new ConsoleDisplayBoard();
//        ParkingLot parkingLot = parkingLotFactory.createNewParkingLot(1, ParkingSpotType.values().length, 1, feeModelList);
//        Vehicle vehicle1 = new Vehicle(1, VehicleType.CAR);
//        CarOwner carOwner1 = new CarOwner("shyam", vehicle1);
//        String parkingTime = "29-May-2022 14:04:07";
//        LocalDateTime parkingTimeLocal = LocalDateTime.parse(parkingTime, formatter);
//        ParkingTicket parkingTicketShyam = carOwner1.parkVehicle(parkingLot, parkingTimeLocal);
//        Vehicle vehicle2 = new Vehicle(2, VehicleType.MOTORCYCLE);
//        CarOwner carOwner2 = new CarOwner("swetha", vehicle2);
//        carOwner2.parkVehicle(parkingLot);
//        Vehicle vehicle3 = new Vehicle(123, VehicleType.TRUCK);
//        CarOwner carOwner3 = new CarOwner("Chinnu",vehicle3);
//        ParkingTicket parkingTicket3 = carOwner3.parkVehicle(parkingLot);
//        displayBoard.displayNumOfVacantSpotsForEachLevel(parkingLot.getAllLevels());
//        String unParkingTime = "29-May-2022 14:04:07";
//        LocalDateTime unParkingTimeLocal = LocalDateTime.parse(unParkingTime, formatter);
//        PaymentTicket paymentTicket =  carOwner1.unparkVehicleAndGetAmount(parkingLot, new CashPayment(carOwner1.getName(), "1"), unParkingTimeLocal);
//        System.out.println("------");
//        System.out.println(paymentTicket);
//        System.out.println("------");
//        displayBoard.displayNumOfVacantSpotsForEachLevel(parkingLot.getAllLevels());
//        carOwner2.unparkVehicleAndGetAmount(parkingLot,new CashPayment(carOwner2.getName(), "1"), LocalDateTime.now().plusHours(5));
//        displayBoard.displayNumOfVacantSpotsForEachLevel(parkingLot.getAllLevels());
//
//
//    }
//}

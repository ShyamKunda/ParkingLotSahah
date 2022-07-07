package Payment;

import Exceptions.InputNotProvidedException;
import Vehicles.VehicleType;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class PaymentCalculation {

    public static long getAmount(List<FeeModel> feeModelList, ParkingTicket parkingTicket, LocalDateTime unparkTime, VehicleType vehicleType) throws InputNotProvidedException {

        TreeMap<Integer, FeeModel> specificVehicleFeeModelTreeMap = new TreeMap<>();
        List<FeeModel> specificVehicleFeeModelList = feeModelList.stream().filter(feeModel -> feeModel.getVehicleType() == vehicleType).collect(Collectors.toList());

        if (specificVehicleFeeModelList.size() == 0) {
            throw new InputNotProvidedException("Fee Model is Not provided for Type : " + vehicleType);
        }

        specificVehicleFeeModelList.forEach(feeModel -> specificVehicleFeeModelTreeMap.put(feeModel.getStart(), feeModel));
        long minutes = ChronoUnit.MINUTES.between(parkingTicket.getParkingTime(),
                unparkTime);
        int hours = (int)Math.ceil((double) minutes/60);
        FeeModel finalFeeModel = hours == 0? specificVehicleFeeModelTreeMap.firstEntry().getValue():
                specificVehicleFeeModelTreeMap.floorEntry((int)hours).getValue();
        int intervalStart = finalFeeModel.getStart();
//        System.out.println("Total Hours: " + hours);
//        System.out.println("Interval start: " + finalFeeModel.getStart() + " Price: " + finalFeeModel.getPrice());
        long totalCost = 0;

        FeeModel lastFeeModel = specificVehicleFeeModelTreeMap.lastEntry().getValue();
        PaymentCriteria paymentCriteria = lastFeeModel.getPaymentCriteria();
        if (lastFeeModel.isIgnorePreviousCriteria()) {
            if (finalFeeModel.getStart() == specificVehicleFeeModelTreeMap.lastKey()) {
                if (finalFeeModel.getPaymentCriteria() == PaymentCriteria.DAYS) {
                    int days = hours/24;
                    int costForOneDay = finalFeeModel.getPrice();
                    totalCost = costForOneDay*days;
                } else {
                    int totalHours = hours -finalFeeModel.getStart();
                    totalCost = totalHours*finalFeeModel.getPrice();
                }
            } else {
                totalCost = finalFeeModel.getPrice();
            }
        } else {
            if (finalFeeModel.getStart() == specificVehicleFeeModelTreeMap.lastKey()) {
//            System.out.println("This is last key");
                for (FeeModel feeModel : specificVehicleFeeModelList) {
                    totalCost+= feeModel.getPrice();
                    if (feeModel.getStart() == intervalStart) {
                        break;
                    }
                }
                totalCost = totalCost - specificVehicleFeeModelTreeMap.lastEntry().getValue().getPrice();
//            System.out.println("Previous cost computations done " + totalCost );
                long remainingHours = hours - specificVehicleFeeModelTreeMap.lastKey();
//            System.out.println("Remaining hours: " +  remainingHours);
                long costForRemainingHours = remainingHours* specificVehicleFeeModelTreeMap.lastEntry().getValue().getPrice();
                totalCost = totalCost + costForRemainingHours;
                System.out.println(totalCost);
//            System.out.println("Total cost " + totalCost);
            } else {
//            System.out.println("This is not last key");
                for (FeeModel feeModel : specificVehicleFeeModelList) {
                    totalCost+= feeModel.getPrice();
                    if (feeModel.getStart() == intervalStart) {
                        break;
                    }
                }
                System.out.println("Total cost " + totalCost);
            }
        }

        return totalCost;
    }
}


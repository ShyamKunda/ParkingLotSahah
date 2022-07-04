package Payment;

import Vehicles.VehicleType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class PaymentCalculation {
    public static long getAmount(List<FeeModel> feeModelList, long hours, VehicleType vehicleType) {
        TreeMap<Integer, FeeModel> specificVehicleFeeModelTreeMap = new TreeMap<>();
        List<FeeModel> specificVehicleFeeModelList = feeModelList.stream().filter(feeModel -> feeModel.getVehicleType() == vehicleType).collect(Collectors.toList());

        specificVehicleFeeModelList.forEach(feeModel -> specificVehicleFeeModelTreeMap.put(feeModel.getStart(), feeModel));
        FeeModel finalFeeModel = specificVehicleFeeModelTreeMap.floorEntry((int)hours).getValue();
        int intervalStart = finalFeeModel.getStart();
        System.out.println("Total Hours: " + hours);
        System.out.println("Interval start: " + finalFeeModel.getStart() + " Price: " + finalFeeModel.getPrice());
        long totalCost = 0;
        if (finalFeeModel.getStart() == specificVehicleFeeModelTreeMap.lastKey()) {
            System.out.println("This is last key");
            for (FeeModel feeModel : specificVehicleFeeModelList) {
                totalCost+= feeModel.getPrice();
                if (feeModel.getStart() == intervalStart) {
                    break;
                }
            }
            totalCost = totalCost - specificVehicleFeeModelTreeMap.lastEntry().getValue().getPrice();
            System.out.println("Previous cost computations done " + totalCost );
            long remainingHours = hours - specificVehicleFeeModelTreeMap.lastKey();
            System.out.println("Remaining hours: " +  remainingHours);
            long costForRemainingHours = remainingHours* specificVehicleFeeModelTreeMap.lastEntry().getValue().getPrice();
            totalCost = totalCost + costForRemainingHours;
            System.out.println(totalCost);
            System.out.println("Total cost " + totalCost);
        } else {
            System.out.println("This is not last key");
            for (FeeModel feeModel : specificVehicleFeeModelList) {
                totalCost+= feeModel.getPrice();
                if (feeModel.getStart() == intervalStart) {
                    break;
                }
            }
            System.out.println("Total cost " + totalCost);
        }
        return totalCost;
    }
}


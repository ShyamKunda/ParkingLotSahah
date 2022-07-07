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
        int hours = (int)Math.floor((double) minutes/60.0);
        double hoursDouble = (double) minutes/60.0;
        int hourCeiling = (int)Math.ceil(hoursDouble);
        int hourFloor = (int)Math.floor(hoursDouble);
        int lastFeeKey = specificVehicleFeeModelTreeMap.lastEntry().getValue().getStart(); //12
        FeeModel finalFeeModel ;
        if (hoursDouble >= (double) lastFeeKey ) {
            finalFeeModel = specificVehicleFeeModelTreeMap.floorEntry(hourCeiling).getValue();
        } else {
            finalFeeModel = specificVehicleFeeModelTreeMap.floorEntry(hourFloor).getValue();
        }



        int intervalStart = finalFeeModel.getStart();
        long totalCost = 0;

        FeeModel lastFeeModel = specificVehicleFeeModelTreeMap.lastEntry().getValue();
        PaymentCriteria paymentCriteria = lastFeeModel.getPaymentCriteria();
        if (lastFeeModel.isIgnorePreviousCriteria()) {
            if (finalFeeModel.getStart() == specificVehicleFeeModelTreeMap.lastKey()) {
                if (finalFeeModel.getPaymentCriteria() == PaymentCriteria.DAYS) {
                    int days =  (int) Math.ceil(hours/24.0);
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
                for (FeeModel feeModel : specificVehicleFeeModelList) {
                    totalCost+= feeModel.getPrice();
                    if (feeModel.getStart() == intervalStart) {
                        break;
                    }
                }
                totalCost = totalCost - specificVehicleFeeModelTreeMap.lastEntry().getValue().getPrice();
                long remainingHours = hourCeiling - specificVehicleFeeModelTreeMap.lastKey();
                long costForRemainingHours = remainingHours* specificVehicleFeeModelTreeMap.lastEntry().getValue().getPrice();
                totalCost = totalCost + costForRemainingHours;
                System.out.println(totalCost);
            } else {
                for (FeeModel feeModel : specificVehicleFeeModelList) {
                    totalCost+= feeModel.getPrice();
                    if (feeModel.getStart() == intervalStart) {
                        break;
                    }
                }
            }
        }

        return totalCost;
    }
}


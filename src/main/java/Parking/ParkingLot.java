package Parking;

import Exceptions.InvalidParkingTicketException;
import Exceptions.InvalidPaymentTicketException;
import Exceptions.ParkingLotIsFullException;
import Parking.Policies.LevelAssignmentPolicy;
import Parking.Policies.ParkingAssignmentPolicy;
import Payment.*;
import Vehicles.Vehicle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ParkingLot implements ParkingLotFacade{
    private ParkingLevelsCollection fullLevels;
    private ParkingLevelsCollection nonFullLevels;
    private LevelAssignmentPolicy levelAssignmentPolicy;
    private ParkingAssignmentPolicy parkingAssignmentPolicy;
    private PaymentCalculation paymentCalculation;

    private List<FeeModel> feeModelList;

    public ParkingLot
            (ParkingLevelsCollection allLevels,
             LevelAssignmentPolicy levelAssignmentPolicy,
             ParkingAssignmentPolicy parkingAssignmentPolicy,
             PaymentCalculation paymentCalculation) {
        this.nonFullLevels = allLevels;
        this.levelAssignmentPolicy = levelAssignmentPolicy;
        this.parkingAssignmentPolicy = parkingAssignmentPolicy;
        this.paymentCalculation = paymentCalculation;
        this.fullLevels= new ArrayListParkingLevelsCollection();
    }

    public ParkingLot
            (ParkingLevelsCollection allLevels,
             LevelAssignmentPolicy levelAssignmentPolicy,
             ParkingAssignmentPolicy parkingAssignmentPolicy,
             List<FeeModel> feeModelList) {
        this.nonFullLevels = allLevels;
        this.levelAssignmentPolicy = levelAssignmentPolicy;
        this.parkingAssignmentPolicy = parkingAssignmentPolicy;
        this.feeModelList = feeModelList;
        this.fullLevels= new ArrayListParkingLevelsCollection();
    }

    public ParkingTicket parkVehicle(Vehicle vehicle) throws ParkingLotIsFullException {
        if (nonFullLevels.size() == 0) {
            throw new ParkingLotIsFullException("No vacant levels for parking");
        }
        ParkingLevel assignedParkingLevel = levelAssignmentPolicy.assignLevel(nonFullLevels, vehicle);
        ParkingSpot assignedParkingSpot =
                assignedParkingLevel.parkVehicle(vehicle, parkingAssignmentPolicy);

        if (assignedParkingLevel.getTotalNumOfVacantSpots() == 0) {
            nonFullLevels.remove(assignedParkingLevel);
            fullLevels.add(assignedParkingLevel);
        }
        return generateParkingTicket(vehicle, assignedParkingSpot);
    }

    public ParkingTicket parkVehicle(Vehicle vehicle, LocalDateTime localDateTime) throws ParkingLotIsFullException {
        if (nonFullLevels.size() == 0) {
            throw new ParkingLotIsFullException("No vacant levels for parking");
        }
        ParkingLevel assignedParkingLevel = levelAssignmentPolicy.assignLevel(nonFullLevels, vehicle);
        ParkingSpot assignedParkingSpot =
                assignedParkingLevel.parkVehicle(vehicle, parkingAssignmentPolicy);

        if (assignedParkingLevel.getTotalNumOfVacantSpots() == 0) {
            nonFullLevels.remove(assignedParkingLevel);
            fullLevels.add(assignedParkingLevel);
        }
        return generateParkingTicket(vehicle, assignedParkingSpot, localDateTime);
    }

    public void unparkVehicle(Vehicle vehicle, PaymentTicket paymentTicket) throws InvalidPaymentTicketException {
        if (paymentTicket == null) {
            throw new InvalidPaymentTicketException("Invalid payment ticket " +
                    "was passed");
        }
        int parkingLevelID = paymentTicket.getParkingSpot().getParkingLevelID();
        ParkingLevel parkingLevel = getLevel(parkingLevelID);
        if (parkingLevel.getTotalNumOfVacantSpots() == 0) {
            fullLevels.remove(parkingLevel);
            nonFullLevels.add(parkingLevel);
        }
        parkingLevel.unparkVehicle(vehicle, paymentTicket);
    }

    public int unparkVehicleAndGetAmount(Vehicle vehicle, PaymentTicket paymentTicket) throws InvalidPaymentTicketException {
        if (paymentTicket == null) {
            throw new InvalidPaymentTicketException("Invalid payment ticket " +
                    "was passed");
        }
        int parkingLevelID = paymentTicket.getParkingSpot().getParkingLevelID();
        ParkingLevel parkingLevel = getLevel(parkingLevelID);
        if (parkingLevel.getTotalNumOfVacantSpots() == 0) {
            fullLevels.remove(parkingLevel);
            nonFullLevels.add(parkingLevel);
        }
        parkingLevel.unparkVehicle(vehicle, paymentTicket);
        return 0;
    }

    public PaymentTicket pay(ParkingTicket parkingTicket, LocalDateTime unParkingTime, Vehicle vehicle) throws InvalidParkingTicketException {
        if (parkingTicket==null) {
            throw new InvalidParkingTicketException("an invalid parking " +
                    "ticket was passed");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
        long minutes = ChronoUnit.MINUTES.between(parkingTicket.getParkingTime(),
                unParkingTime);
        int hours = (int)Math.ceil((double) minutes/60);
        System.out.println("Hours : " + hours);
        double paymentSum = PaymentCalculation.getAmount(feeModelList, hours, vehicle.getVehicleType());
        return generatePaymentTicket(parkingTicket, paymentSum, unParkingTime);
    }


    private ParkingTicket generateParkingTicket(Vehicle vehicle, ParkingSpot parkingSpot) {
        return new ParkingTicket(vehicle.getID(), parkingSpot, LocalDateTime.now());
    }

    private ParkingTicket generateParkingTicket(Vehicle vehicle, ParkingSpot parkingSpot, LocalDateTime localDateTime) {
        return new ParkingTicket(vehicle.getID(), parkingSpot, localDateTime);
    }

    private PaymentTicket generatePaymentTicket(ParkingTicket parkingTicket, double paymentSum) {
        return new PaymentTicket(parkingTicket.getVehicleID(),
                parkingTicket.getParkingSpot(),
                parkingTicket.getParkingTime(),
                LocalDateTime.now(),
                paymentSum);
    }

    private PaymentTicket generatePaymentTicket(ParkingTicket parkingTicket, double paymentSum, LocalDateTime unParkingTime) {
        return new PaymentTicket(parkingTicket.getVehicleID(),
                parkingTicket.getParkingSpot(),
                parkingTicket.getParkingTime(),
                unParkingTime,
                paymentSum);
    }


    //admin

    public void setLevelSelectionPolicy(LevelAssignmentPolicy levelAssignmentPolicy) {
        this.levelAssignmentPolicy = levelAssignmentPolicy;
    }

    public void setPaymentPolicy(PaymentCalculation paymentCalculation) {
        this.paymentCalculation = paymentCalculation;
    }

    public ParkingLevel getLevel(int parkingLevelID) {
        ParkingLevel parkingLevel = fullLevels.getParkingLevel(parkingLevelID);
        if (parkingLevel != null) {
            return parkingLevel;
        }
        return nonFullLevels.getParkingLevel((parkingLevelID));
    }

    public void addLevel(ParkingLevel parkingLevel) {
        nonFullLevels.add(parkingLevel);
    }

    public void removeLevel(ParkingLevel parkingLevel) {
        fullLevels.remove(parkingLevel);
        nonFullLevels.remove(parkingLevel);
    }


    public ParkingLevelsCollection getAllLevels(){
        ParkingLevelsCollection combined= new ArrayListParkingLevelsCollection();
        combined.addAll(nonFullLevels);
        combined.addAll(fullLevels);
        return combined;
    }
}

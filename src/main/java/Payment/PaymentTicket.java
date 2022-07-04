package Payment;

import Parking.ParkingSpot;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class PaymentTicket {
    private int vehicleID;
    private ParkingSpot parkingSpot;
    private LocalDateTime parkingTime;
    private LocalDateTime unparkingTime;
    private double paymentSum;

    private long hours;

    @Override
    public String toString() {
        long minutes = ChronoUnit.MINUTES.between(parkingTime,
                unparkingTime);
        hours = (int)Math.ceil((double) minutes/60);
        return "PaymentTicket{" +
                "\nvehicleID=" + vehicleID +
                ",\nparkingSpot=" + parkingSpot +
                ",\nparkingTime=" + parkingTime +
                ",\nunparkingTime=" + unparkingTime +
                ",\nTotal hours=" + hours +
                ",\npaymentSum=" + paymentSum +
                "\n}";
    }

    public PaymentTicket(int vehicleID, ParkingSpot parkingSpot,
                         LocalDateTime parkingTime, LocalDateTime unparkingTime,
                         double paymentSum) {
        this.vehicleID = vehicleID;
        this.parkingSpot = parkingSpot;
        this.parkingTime = parkingTime;
        this.unparkingTime = unparkingTime;
        this.paymentSum = paymentSum;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public LocalDateTime getParkingTime() {
        return parkingTime;
    }

    public LocalDateTime getUnparkingTime() {
        return unparkingTime;
    }

    public double getPaymentSum() {
        return paymentSum;
    }
}

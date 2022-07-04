package Users;

import Exceptions.InvalidParkingTicketException;
import Exceptions.InvalidPaymentTicketException;
import Exceptions.ParkingLotIsFullException;
import Parking.ParkingLotFacade;
import Payment.ParkingTicket;
import Payment.PaymentMethod;
import Payment.PaymentTicket;
import Vehicles.Vehicle;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class CarOwner {
    String name;
    Vehicle vehicle;
    ParkingTicket parkingTicket;
    PaymentTicket paymentTicket;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public ParkingTicket getParkingTicket() {
        return parkingTicket;
    }

    public void setParkingTicket(ParkingTicket parkingTicket) {
        this.parkingTicket = parkingTicket;
    }

    public PaymentTicket getPaymentTicket() {
        return paymentTicket;
    }

    public void setPaymentTicket(PaymentTicket paymentTicket) {
        this.paymentTicket = paymentTicket;
    }

    public CarOwner(String name, Vehicle vehicle) {
        this.name = name;
        this.vehicle = vehicle;
        this.parkingTicket = null;
        this.paymentTicket = null;
    }

    public ParkingTicket parkVehicle(ParkingLotFacade parkingLot) {
        int numOfTries = 5;
        while (numOfTries > 0) {
            try {
                this.parkingTicket = parkingLot.parkVehicle(vehicle);
                break;
            } catch (ParkingLotIsFullException parkingLotIsFullException) {
                handleFullParkingLotException(parkingLotIsFullException);
            }
            numOfTries--;
        }
        return this.parkingTicket;
    }

    public ParkingTicket parkVehicle(ParkingLotFacade parkingLot, LocalDateTime parkingTime) {
        int numOfTries = 5;
        while (numOfTries > 0) {
            try {
                this.parkingTicket = parkingLot.parkVehicle(vehicle, parkingTime);
                break;
            } catch (ParkingLotIsFullException parkingLotIsFullException) {
                handleFullParkingLotException(parkingLotIsFullException);
            }
            numOfTries--;
        }
        return this.parkingTicket;
    }

    private void handleFullParkingLotException(ParkingLotIsFullException parkingLotIsFullException) {
        parkingLotIsFullException.printStackTrace();
        try {
            TimeUnit.MINUTES.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public PaymentTicket pay(ParkingLotFacade parkingLot, LocalDateTime unParkTime, Vehicle vehicle) {

        try {
            this.paymentTicket = parkingLot.pay(this.parkingTicket, unParkTime, vehicle);
        } catch (InvalidParkingTicketException e) {
            e.printStackTrace();
        }
        return this.paymentTicket;
    }

    public void unparkVehicle(ParkingLotFacade parkingLot) {
        try {
            parkingLot.unparkVehicle(this.vehicle, this.paymentTicket);
        }
        catch (InvalidPaymentTicketException e) {
            e.printStackTrace();
        }
    }

    public PaymentTicket unparkVehicleAndGetAmount(ParkingLotFacade parkingLot, LocalDateTime unParkTime) {
        try {
            this.paymentTicket =this.pay(parkingLot, unParkTime, this.vehicle);
            parkingLot.unparkVehicleAndGetAmount(this.vehicle, this.paymentTicket);
        }
        catch (InvalidPaymentTicketException e) {
            e.printStackTrace();
        }
        return paymentTicket;
    }


}

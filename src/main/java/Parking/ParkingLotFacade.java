package Parking;

import Exceptions.InputNotProvidedException;
import Exceptions.InvalidParkingTicketException;
import Exceptions.InvalidPaymentTicketException;
import Exceptions.ParkingLotIsFullException;
import Payment.ParkingTicket;
import Payment.PaymentTicket;
import Vehicles.Vehicle;

import java.time.LocalDateTime;

public interface ParkingLotFacade {
    ParkingTicket parkVehicle(Vehicle vehicle) throws ParkingLotIsFullException;

    ParkingTicket parkVehicle(Vehicle vehicle, LocalDateTime dateTime) throws ParkingLotIsFullException;
    void unparkVehicle(Vehicle vehicle, PaymentTicket paymentTicket) throws InvalidPaymentTicketException;

    int unparkVehicleAndGetAmount(Vehicle vehicle, PaymentTicket paymentTicket) throws InvalidPaymentTicketException;

    PaymentTicket pay(ParkingTicket parkingTicket, LocalDateTime unParkingTime, Vehicle vehicle) throws InvalidParkingTicketException, InputNotProvidedException;
}

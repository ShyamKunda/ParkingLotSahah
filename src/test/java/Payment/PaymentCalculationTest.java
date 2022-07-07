package Payment;

import Exceptions.InvalidParkingLevelException;
import Parking.Factories.ParkingLotFactory;
import Parking.ParkingLot;
import Parking.ParkingSpotType;
import Users.VehicleOwner;
import Vehicles.Vehicle;
import Vehicles.VehicleType;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PaymentCalculationTest {

    private ParkingLotFactory parkingLotFactory;
    private Map<ParkingSpotType, Integer> spotAllocations;
    private DateTimeFormatter formatter;
    private List<FeeModel> mallFlatRates;
    private List<FeeModel> stadiumFeeList;
    List<FeeModel> airportFeeModel;


    @BeforeEach
    void setUp() {
        parkingLotFactory = new ParkingLotFactory();
        formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
        spotAllocations = new HashMap<>();
        spotAllocations.put(ParkingSpotType.MOTORCYCLE, 5);
        spotAllocations.put(ParkingSpotType.CAR, 4);
        spotAllocations.put(ParkingSpotType.TRUCK, 3);
        formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");

        stadiumFeeList = new ArrayList<>();
        stadiumFeeList.add(new FeeModel(0,3,30, VehicleType.MOTORCYCLE, PaymentCriteria.HOURS));
        stadiumFeeList.add(new FeeModel(4,11,60, VehicleType.MOTORCYCLE, PaymentCriteria.HOURS));
        stadiumFeeList.add(new FeeModel(12,-1,100, VehicleType.MOTORCYCLE, PaymentCriteria.HOURS));
        stadiumFeeList.add(new FeeModel(0,3,60, VehicleType.CAR, PaymentCriteria.HOURS));
        stadiumFeeList.add(new FeeModel(4,12,120, VehicleType.CAR, PaymentCriteria.HOURS));
        stadiumFeeList.add(new FeeModel(12,-1,200, VehicleType.CAR, PaymentCriteria.HOURS, true));

        mallFlatRates = new ArrayList<>();
        FeeModel.Builder feeModelBuilder = FeeModel.Builder.newInstance();
        mallFlatRates.add(feeModelBuilder.setStart(0).setEnd(1).setPrice(10).setVehicleType(VehicleType.MOTORCYCLE)
                .setPaymentCriteria(PaymentCriteria.HOURS).build());
        mallFlatRates.add(feeModelBuilder.setStart(0).setEnd(1).setPrice(20).setVehicleType(VehicleType.CAR)
                .setPaymentCriteria(PaymentCriteria.HOURS).build());
        mallFlatRates.add(feeModelBuilder.setStart(0).setEnd(1).setPrice(50).setVehicleType(VehicleType.TRUCK)
                .setPaymentCriteria(PaymentCriteria.HOURS).build());


        airportFeeModel = new ArrayList<>();
        airportFeeModel.add(new FeeModel(0,1,0, VehicleType.MOTORCYCLE, PaymentCriteria.HOURS));
        airportFeeModel.add(new FeeModel(1,7,40, VehicleType.MOTORCYCLE, PaymentCriteria.HOURS));
        airportFeeModel.add(new FeeModel(8,24,60, VehicleType.MOTORCYCLE, PaymentCriteria.HOURS));
        airportFeeModel.add(new FeeModel(24,-1,80, VehicleType.MOTORCYCLE, PaymentCriteria.DAYS, true, true));
        airportFeeModel.add(new FeeModel(0,11,60, VehicleType.CAR, PaymentCriteria.HOURS));
        airportFeeModel.add(new FeeModel(12,24,80, VehicleType.CAR, PaymentCriteria.HOURS));
        airportFeeModel.add(new FeeModel(24,-1,100, VehicleType.CAR, PaymentCriteria.DAYS, true, true));


    }



    @DisplayName("Verify Amount Per Hour Flat Fees")
    @Test @Order(1)
    void testPerHourFlatFees() throws InvalidParkingLevelException {


        ParkingLot parkingLot = parkingLotFactory.createNewParkingLot(1, ParkingSpotType.values().length, spotAllocations, mallFlatRates);
        Vehicle vehicle = new Vehicle(12345, VehicleType.MOTORCYCLE);
        VehicleOwner cycleOwner = new VehicleOwner("Shyam", vehicle);
        cycleOwner.parkVehicle(parkingLot, LocalDateTime.parse("29-May-2022 14:04:07", formatter));
        PaymentTicket paymentTicket= cycleOwner.unparkVehicleAndGetAmount(parkingLot, LocalDateTime.parse("29-May-2022 14:04:07", formatter).plusHours(4));
        assertThat(paymentTicket.getPaymentSum()).isEqualTo(40.0);
        Vehicle car = new Vehicle(12346, VehicleType.CAR);
        VehicleOwner carOwner = new VehicleOwner("Yuvaraj", car);
        carOwner.parkVehicle(parkingLot, LocalDateTime.parse("29-May-2022 14:04:07", formatter));
        PaymentTicket paymentTicket1= carOwner.unparkVehicleAndGetAmount(parkingLot, LocalDateTime.parse("29-May-2022 14:04:07", formatter).plusHours(4));
        assertThat(paymentTicket1.getPaymentSum()).isEqualTo(80);
    }

    @DisplayName("Stadium fee model : bike : 3:40 hours")
    @Test @Order(2)
    void testDifferentCostForEachInterval() throws InvalidParkingLevelException {
        ParkingLot parkingLot = parkingLotFactory.createNewParkingLot(1, ParkingSpotType.values().length, spotAllocations, stadiumFeeList);
        Vehicle vehicle = new Vehicle(12345, VehicleType.MOTORCYCLE);
        VehicleOwner cycleOwner = new VehicleOwner("Shyam", vehicle);
        cycleOwner.parkVehicle(parkingLot, LocalDateTime.parse("29-May-2022 14:00:00", formatter));
        PaymentTicket paymentTicket= cycleOwner.unparkVehicleAndGetAmount(parkingLot, LocalDateTime.parse("29-May-2022 14:00:00", formatter).plusHours(3).plusMinutes(30));
        assertThat(paymentTicket.getPaymentSum()).isEqualTo(30.0);
    }

    @DisplayName("Stadium fee model : bike : 14:59 hours")
    @Test @Order(2)
    void testDifferentCostForEachInterval1() throws InvalidParkingLevelException {
        ParkingLot parkingLot = parkingLotFactory.createNewParkingLot(1, ParkingSpotType.values().length, spotAllocations, stadiumFeeList);
        Vehicle vehicle = new Vehicle(12345, VehicleType.MOTORCYCLE);
        VehicleOwner cycleOwner = new VehicleOwner("Shyam", vehicle);
        cycleOwner.parkVehicle(parkingLot, LocalDateTime.parse("29-May-2022 14:00:00", formatter));
        PaymentTicket paymentTicket= cycleOwner.unparkVehicleAndGetAmount(parkingLot, LocalDateTime.parse("29-May-2022 14:00:00", formatter).plusHours(14).plusMinutes(59));
        assertThat(paymentTicket.getPaymentSum()).isEqualTo(390);
    }

    @DisplayName("Stadium fee model : bike : 14:59 hours")
    @Test @Order(3)
    void testDifferentCostForEachInterval3() throws InvalidParkingLevelException {
        ParkingLot parkingLot = parkingLotFactory.createNewParkingLot(1, ParkingSpotType.values().length, spotAllocations, stadiumFeeList);
        Vehicle vehicle = new Vehicle(12345, VehicleType.MOTORCYCLE);
        VehicleOwner cycleOwner = new VehicleOwner("Shyam", vehicle);
        cycleOwner.parkVehicle(parkingLot, LocalDateTime.parse("29-May-2022 14:00:00", formatter));
        PaymentTicket paymentTicket= cycleOwner.unparkVehicleAndGetAmount(parkingLot, LocalDateTime.parse("29-May-2022 14:00:00", formatter).plusHours(14).plusMinutes(59));
        assertThat(paymentTicket.getPaymentSum()).isEqualTo(390);
    }

    @DisplayName("Stadium fee model : SUV : 11:30 hours")
    @Test @Order(3)
    void testDifferentCostForEachInterval4() throws InvalidParkingLevelException {
        ParkingLot parkingLot = parkingLotFactory.createNewParkingLot(1, ParkingSpotType.values().length, spotAllocations, stadiumFeeList);
        Vehicle vehicle = new Vehicle(12345, VehicleType.CAR);
        VehicleOwner cycleOwner = new VehicleOwner("Shyam", vehicle);
        cycleOwner.parkVehicle(parkingLot, LocalDateTime.parse("29-May-2022 14:00:00", formatter));
        PaymentTicket paymentTicket= cycleOwner.unparkVehicleAndGetAmount(parkingLot, LocalDateTime.parse("29-May-2022 14:00:00", formatter).plusHours(11).plusMinutes(30));
        assertThat(paymentTicket.getPaymentSum()).isEqualTo(180);
    }

    @DisplayName("Stadium fee model : SUV : 13:05 hours")
    @Test @Order(4)
    void testDifferentCostForEachInterval5() throws InvalidParkingLevelException {
        ParkingLot parkingLot = parkingLotFactory.createNewParkingLot(1, ParkingSpotType.values().length, spotAllocations, stadiumFeeList);
        Vehicle vehicle = new Vehicle(12345, VehicleType.CAR);
        VehicleOwner cycleOwner = new VehicleOwner("Shyam", vehicle);
        cycleOwner.parkVehicle(parkingLot, LocalDateTime.parse("29-May-2022 14:00:00", formatter));
        PaymentTicket paymentTicket= cycleOwner.unparkVehicleAndGetAmount(parkingLot, LocalDateTime.parse("29-May-2022 14:00:00", formatter).plusHours(13).plusMinutes(5));
        assertThat(paymentTicket.getPaymentSum()).isEqualTo(580);
    }

    @DisplayName("Airport fee model : BIKE : 55 minutes")
    @Test @Order(4)
    void testDifferentCostForEachInterval6() throws InvalidParkingLevelException {
        ParkingLot parkingLot = parkingLotFactory.createNewParkingLot(1, ParkingSpotType.values().length, spotAllocations, airportFeeModel);
        Vehicle vehicle = new Vehicle(12345, VehicleType.MOTORCYCLE);
        VehicleOwner cycleOwner = new VehicleOwner("Shyam", vehicle);
        cycleOwner.parkVehicle(parkingLot, LocalDateTime.parse("29-May-2022 14:00:00", formatter));
        PaymentTicket paymentTicket= cycleOwner.unparkVehicleAndGetAmount(parkingLot, LocalDateTime.parse("29-May-2022 14:00:00", formatter).plusMinutes(55));
        assertThat(paymentTicket.getPaymentSum()).isEqualTo(0);
    }

    @DisplayName("Airport fee model : BIKE : 14:59 minutes")
    @Test @Order(5)
    void testDifferentCostForEachInterval7() throws InvalidParkingLevelException {
        ParkingLot parkingLot = parkingLotFactory.createNewParkingLot(1, ParkingSpotType.values().length, spotAllocations, airportFeeModel);
        Vehicle vehicle = new Vehicle(12345, VehicleType.MOTORCYCLE);
        VehicleOwner cycleOwner = new VehicleOwner("Shyam", vehicle);
        cycleOwner.parkVehicle(parkingLot, LocalDateTime.parse("29-May-2022 14:00:00", formatter));
        PaymentTicket paymentTicket= cycleOwner.unparkVehicleAndGetAmount(parkingLot, LocalDateTime.parse("29-May-2022 14:00:00", formatter).plusHours(14).plusMinutes(55));
        assertThat(paymentTicket.getPaymentSum()).isEqualTo(60);
    }

    @DisplayName("Airport fee model : BIKE : 1 day 12 hours")
    @Test @Order(6)
    void testDifferentCostForEachInterval8() throws InvalidParkingLevelException {
        ParkingLot parkingLot = parkingLotFactory.createNewParkingLot(1, ParkingSpotType.values().length, spotAllocations, airportFeeModel);
        Vehicle vehicle = new Vehicle(12345, VehicleType.MOTORCYCLE);
        VehicleOwner cycleOwner = new VehicleOwner("Shyam", vehicle);
        cycleOwner.parkVehicle(parkingLot, LocalDateTime.parse("29-May-2022 14:00:00", formatter));
        PaymentTicket paymentTicket= cycleOwner.unparkVehicleAndGetAmount(parkingLot, LocalDateTime.parse("29-May-2022 14:00:00", formatter).plusDays(1).plusHours(12));
        assertThat(paymentTicket.getPaymentSum()).isEqualTo(160);
    }

    @DisplayName("Airport fee model : CAR : 50 min")
    @Test @Order(7)
    void testDifferentCostForEachInterval9() throws InvalidParkingLevelException {
        ParkingLot parkingLot = parkingLotFactory.createNewParkingLot(1, ParkingSpotType.values().length, spotAllocations, airportFeeModel);
        Vehicle vehicle = new Vehicle(12345, VehicleType.CAR);
        VehicleOwner cycleOwner = new VehicleOwner("Shyam", vehicle);
        cycleOwner.parkVehicle(parkingLot, LocalDateTime.parse("29-May-2022 14:00:00", formatter));
        PaymentTicket paymentTicket= cycleOwner.unparkVehicleAndGetAmount(parkingLot, LocalDateTime.parse("29-May-2022 14:00:00", formatter).plusMinutes(50));
        assertThat(paymentTicket.getPaymentSum()).isEqualTo(60);
    }

    @DisplayName("Airport fee model : CAR : 23:59 hours ")
    @Test @Order(8)
    void testDifferentCostForEachInterval10() throws InvalidParkingLevelException {
        ParkingLot parkingLot = parkingLotFactory.createNewParkingLot(1, ParkingSpotType.values().length, spotAllocations, airportFeeModel);
        Vehicle vehicle = new Vehicle(12345, VehicleType.CAR);
        VehicleOwner cycleOwner = new VehicleOwner("Shyam", vehicle);
        cycleOwner.parkVehicle(parkingLot, LocalDateTime.parse("29-May-2022 14:00:00", formatter));
        PaymentTicket paymentTicket= cycleOwner.unparkVehicleAndGetAmount(parkingLot, LocalDateTime.parse("29-May-2022 14:00:00", formatter).plusHours(23).plusMinutes(59));
        assertThat(paymentTicket.getPaymentSum()).isEqualTo(80);
    }

    @DisplayName("Airport fee model :CAR : 3 days 1 hour")
    @Test @Order(9)
    void testDifferentCostForEachInterval11() throws InvalidParkingLevelException {
        ParkingLot parkingLot = parkingLotFactory.createNewParkingLot(1, ParkingSpotType.values().length, spotAllocations, airportFeeModel);
        Vehicle vehicle = new Vehicle(12345, VehicleType.CAR);
        VehicleOwner cycleOwner = new VehicleOwner("Shyam", vehicle);
        cycleOwner.parkVehicle(parkingLot, LocalDateTime.parse("29-May-2022 14:00:00", formatter));
        PaymentTicket paymentTicket= cycleOwner.unparkVehicleAndGetAmount(parkingLot, LocalDateTime.parse("29-May-2022 14:00:00", formatter).plusDays(3).plusHours(1));
        assertThat(paymentTicket.getPaymentSum()).isEqualTo(400);
    }



}

package Parking.Factories;

import Exceptions.InvalidParkingLevelException;
import Parking.*;
import Payment.FeeModel;
import Payment.ParkingTicket;
import Payment.PaymentCriteria;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ParkingLotFactoryTest {

    ParkingLotFactory parkingLotFactory;
    Map<ParkingSpotType, Integer> spotAllocations;
    List<FeeModel> feeModelList = new ArrayList<>();
    DateTimeFormatter formatter;


    @BeforeEach
    void setUp() {
        parkingLotFactory = new ParkingLotFactory();
        formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
        spotAllocations = new HashMap<>();
        spotAllocations.put(ParkingSpotType.MOTORCYCLE, 5);
        spotAllocations.put(ParkingSpotType.CAR, 4);
        spotAllocations.put(ParkingSpotType.TRUCK, 3);
        FeeModel.Builder feeModelBuilder = FeeModel.Builder.newInstance();
        feeModelList.add(feeModelBuilder.setStart(0).setEnd(1).setPrice(0).setVehicleType(VehicleType.MOTORCYCLE)
                .setPaymentCriteria(PaymentCriteria.HOURS).build());
        feeModelList.add(feeModelBuilder.setStart(1).setEnd(7).setPrice(40).setVehicleType(VehicleType.MOTORCYCLE)
                .setPaymentCriteria(PaymentCriteria.HOURS).build());
        feeModelList.add(new FeeModel(1,7,40, VehicleType.MOTORCYCLE, PaymentCriteria.HOURS));
        feeModelList.add(new FeeModel(8,24,60, VehicleType.MOTORCYCLE, PaymentCriteria.HOURS));
        feeModelList.add(new FeeModel(24,-1,80, VehicleType.MOTORCYCLE, PaymentCriteria.DAYS));
        feeModelList.add(new FeeModel(0,11,60, VehicleType.CAR, PaymentCriteria.HOURS));
        feeModelList.add(new FeeModel(12,24,80, VehicleType.CAR, PaymentCriteria.HOURS));
        feeModelList.add(new FeeModel(24,-1,100, VehicleType.CAR, PaymentCriteria.DAYS, true, true));
        feeModelList.add(new FeeModel(1,7,40, VehicleType.TRUCK, PaymentCriteria.HOURS));
        feeModelList.add(new FeeModel(8,24,60, VehicleType.TRUCK, PaymentCriteria.HOURS));
        feeModelList.add(new FeeModel(24,-1,80, VehicleType.TRUCK, PaymentCriteria.DAYS));
    }

    /**
     * 1. Create a Parking lot with 2 level and verify levels created
     * 2. Create a Parking lot with 5 level and verify levels created
     */

    @Test @DisplayName("Verify Parking levels Created") @Order(1)
    void testParkingLotIsCreated1() throws InvalidParkingLevelException {
        ParkingLot parkingLot1 = parkingLotFactory.createNewParkingLot(2, ParkingSpotType.values().length, spotAllocations, feeModelList);
        assertThat(parkingLot1.getAllLevels().size()).isEqualTo(2);
        ParkingLot parkingLot2 = parkingLotFactory.createNewParkingLot(5, ParkingSpotType.values().length, spotAllocations, feeModelList);
        assertThat(parkingLot2.getAllLevels().size()).isEqualTo(5);
    }

    /**
     * 1. Create a Parking lot 0 and -1 levels and verify exception
     */
    @DisplayName("Invalid Parking levels not created")
    @Test @Order(2)
    void testParkingLotIsCreated2()  {
        assertThatThrownBy
                (() -> parkingLotFactory.createNewParkingLot(0, ParkingSpotType.values().length, spotAllocations, feeModelList)).isInstanceOf(InvalidParkingLevelException.class)
                .hasMessageContaining("an invalid parking level");
        assertThatThrownBy
                (() -> parkingLotFactory.createNewParkingLot(-1, ParkingSpotType.values().length, spotAllocations, feeModelList)).isInstanceOf(InvalidParkingLevelException.class)
                .hasMessageContaining("an invalid parking level");
    }

    /**
     * Create a Parking lot with 2 levels
     * Validates Even number spots
     * Total spots : 26, Motor Cycle spots : 8, Compact Spots : 6, Large spots : 12,
     * Verify level 0 has 13 vacant spot, Motor Cycle spots : 4, Compact Spots : 3, Large spots : 6
     * Verify level 1 has 13 vacant spot, Motor Cycle spots : 4, Compact Spots : 3, Large spots : 6
     */
    @DisplayName("Verify Total Vacant spots for each type")
    @Test  @Order(3)
    void verifyEvenParkingSpots() throws InvalidParkingLevelException {

        Map<ParkingSpotType, Integer> testSpotAllocations = new HashMap<>();
        testSpotAllocations.put(ParkingSpotType.MOTORCYCLE, 8);
        testSpotAllocations.put(ParkingSpotType.CAR, 6);
        testSpotAllocations.put(ParkingSpotType.TRUCK, 12);
        ParkingLot parkingLot1 = parkingLotFactory.createNewParkingLot(2, ParkingSpotType.values().length, testSpotAllocations, feeModelList);
        ParkingLevel parkingLevel0 =  parkingLot1.getAllLevels().getParkingLevel(0);
        assertThat(parkingLevel0.getID()).isEqualTo(0);
        assertThat(parkingLevel0.getTotalNumOfVacantSpots()).isEqualTo(13);
        assertThat(parkingLevel0.getNumOfVacantSpotsOfType(ParkingSpotType.MOTORCYCLE)).isEqualTo(4);
        assertThat(parkingLevel0.getNumOfVacantSpotsOfType(ParkingSpotType.CAR)).isEqualTo(3);
        assertThat(parkingLevel0.getNumOfVacantSpotsOfType(ParkingSpotType.TRUCK)).isEqualTo(6);
        ParkingLevel parkingLevel1 =  parkingLot1.getAllLevels().getParkingLevel(1);
        assertThat(parkingLevel1.getID()).isEqualTo(1);
        assertThat(parkingLevel1.getTotalNumOfVacantSpots()).isEqualTo(13);
        assertThat(parkingLevel1.getNumOfVacantSpotsOfType(ParkingSpotType.MOTORCYCLE)).isEqualTo(4);
        assertThat(parkingLevel1.getNumOfVacantSpotsOfType(ParkingSpotType.CAR)).isEqualTo(3);
        assertThat(parkingLevel1.getNumOfVacantSpotsOfType(ParkingSpotType.TRUCK)).isEqualTo(6);
    }

    /**
     * Create a Parking lot with 2 levels
     * Validates Odd number spots
     * Total spots : 27, Motor Cycle spots : 7, Compact Spots : 9, Large spots : 11,
     * Verify level 0 has 12 vacant spot, Motor Cycle spots : 3, Compact Spots : 4, Large spots : 5
     * Verify level 1 has 15 vacant spot, Motor Cycle spots : 4, Compact Spots : 5, Large spots : 6
     */
    @DisplayName("Verify Odd Vacant spots for each type")
    @Test @Order(4)
    void verifyOddParkingSpots() throws InvalidParkingLevelException {

        Map<ParkingSpotType, Integer> testSpotAllocations = new HashMap<>();
        testSpotAllocations.put(ParkingSpotType.MOTORCYCLE, 7);
        testSpotAllocations.put(ParkingSpotType.CAR, 9);
        testSpotAllocations.put(ParkingSpotType.TRUCK, 11);
        ParkingLot parkingLot1 = parkingLotFactory.createNewParkingLot(2, ParkingSpotType.values().length, testSpotAllocations, feeModelList);
        ParkingLevel parkingLevel0 =  parkingLot1.getAllLevels().getParkingLevel(0);
        assertThat(parkingLevel0.getTotalNumOfVacantSpots()).isEqualTo(12);
        assertThat(parkingLevel0.getNumOfVacantSpotsOfType(ParkingSpotType.MOTORCYCLE)).isEqualTo(3);
        assertThat(parkingLevel0.getNumOfVacantSpotsOfType(ParkingSpotType.CAR)).isEqualTo(4);
        assertThat(parkingLevel0.getNumOfVacantSpotsOfType(ParkingSpotType.TRUCK)).isEqualTo(5);
        ParkingLevel parkingLevel1 =  parkingLot1.getAllLevels().getParkingLevel(1);
        assertThat(parkingLevel1.getTotalNumOfVacantSpots()).isEqualTo(15);
        assertThat(parkingLevel1.getNumOfVacantSpotsOfType(ParkingSpotType.MOTORCYCLE)).isEqualTo(4);
        assertThat(parkingLevel1.getNumOfVacantSpotsOfType(ParkingSpotType.CAR)).isEqualTo(5);
        assertThat(parkingLevel1.getNumOfVacantSpotsOfType(ParkingSpotType.TRUCK)).isEqualTo(6);
    }

    @DisplayName("Verify Parking spot details after Parking a Vehicle")
    @Test @Order(5)
    void parkAVehicle() throws InvalidParkingLevelException {

        Map<ParkingSpotType, Integer> testSpotAllocations = new HashMap<>();
        testSpotAllocations.put(ParkingSpotType.MOTORCYCLE, 2);
        testSpotAllocations.put(ParkingSpotType.CAR, 9);
        testSpotAllocations.put(ParkingSpotType.TRUCK, 11);
        ParkingLot parkingLot = parkingLotFactory.createNewParkingLot(2, ParkingSpotType.values().length, testSpotAllocations, feeModelList);
        ParkingSpot parkingSpotBike1 = parkingSpotHelper(112233, VehicleType.MOTORCYCLE, "Shyam", parkingLot);
        assertThat(parkingSpotBike1.getParkingSpotType()).isEqualTo(ParkingSpotType.MOTORCYCLE);
        assertThat(parkingSpotBike1.getParkingSpotID()).isEqualTo(0);
        assertThat(parkingSpotBike1.getParkingLevelID()).isEqualTo(0);
        ParkingSpot parkingSpotBike2 = parkingSpotHelper(11228577, VehicleType.MOTORCYCLE, "Sachin", parkingLot);
        assertThat(parkingSpotBike2.getParkingSpotType()).isEqualTo(ParkingSpotType.MOTORCYCLE);
        assertThat(parkingSpotBike2.getParkingSpotID()).isEqualTo(0);
        assertThat(parkingSpotBike2.getParkingLevelID()).isEqualTo(1);
        ParkingSpot parkingSpotCar1 = parkingSpotHelper(21228577, VehicleType.CAR, "Ganguly", parkingLot);
        assertThat(parkingSpotCar1.getParkingSpotType()).isEqualTo(ParkingSpotType.CAR);
        assertThat(parkingSpotCar1.getParkingLevelID()).isEqualTo(0);
        ParkingSpot parkingSpotTruck = parkingSpotHelper(2122877, VehicleType.TRUCK, "Yuvi", parkingLot);
        assertThat(parkingSpotTruck.getParkingSpotType()).isEqualTo(ParkingSpotType.TRUCK);
        assertThat(parkingSpotTruck.getParkingLevelID()).isEqualTo(0);
    }

    @DisplayName("Spot Allocated to next level if parking level is full")
    @Test @Order(6)
    void parkAVehicleInLeve1() throws InvalidParkingLevelException {

        ParkingLotFactory parkingLotFactory = new ParkingLotFactory();
        Map<ParkingSpotType, Integer> testSpotAllocations1 = new HashMap<>();
        testSpotAllocations1.put(ParkingSpotType.MOTORCYCLE, 2);
        testSpotAllocations1.put(ParkingSpotType.CAR, 0);
        testSpotAllocations1.put(ParkingSpotType.TRUCK, 2);
        ParkingLot parkingLot = parkingLotFactory.createNewParkingLot(2, ParkingSpotType.values().length, testSpotAllocations1, feeModelList);
        assertThat(parkingLot.getAllLevels().getParkingLevel(0).getTotalNumOfVacantSpots()).isEqualTo(2);
        assertThat(parkingLot.getAllLevels().getParkingLevel(1).getTotalNumOfVacantSpots()).isEqualTo(2);
        assertThat(parkingLot.getAllLevels().getParkingLevel(1).getNumOfVacantSpotsOfType(ParkingSpotType.MOTORCYCLE)).isEqualTo(1);
        assertThat(parkingLot.getAllLevels().getParkingLevel(1).getNumOfVacantSpotsOfType(ParkingSpotType.TRUCK)).isEqualTo(1);
        ParkingSpot parkingSpotBike1 = parkingSpotHelper(112233, VehicleType.MOTORCYCLE, "Shyam", parkingLot);
        assertThat(parkingSpotBike1.getParkingSpotType()).isEqualTo(ParkingSpotType.MOTORCYCLE);
        assertThat(parkingSpotBike1.getParkingLevelID()).isEqualTo(0);
        ParkingSpot parkingSpotBike2 = parkingSpotHelper(11233, VehicleType.MOTORCYCLE, "Dhone", parkingLot);
        assertThat(parkingSpotBike2.getParkingSpotType()).isEqualTo(ParkingSpotType.MOTORCYCLE);
        assertThat(parkingSpotBike2.getParkingLevelID()).isEqualTo(1);
        ParkingSpot parkingSpotTruck1 = parkingSpotHelper(112233, VehicleType.TRUCK, "Shyam", parkingLot);
        assertThat(parkingSpotTruck1.getParkingSpotType()).isEqualTo(ParkingSpotType.TRUCK);
        assertThat(parkingSpotTruck1.getParkingLevelID()).isEqualTo(0);
        ParkingSpot parkingSpotTruck2 = parkingSpotHelper(112233, VehicleType.TRUCK, "Shyam", parkingLot);
        assertThat(parkingSpotTruck2.getParkingSpotType()).isEqualTo(ParkingSpotType.TRUCK);
        assertThat(parkingSpotTruck2.getParkingLevelID()).isEqualTo(1);
    }

    @DisplayName("Verify Spots Count After And Before Parking Is Full")
    @Test @Order(7)
    void verifySpotsCountAfterAndBeforeParkingIsFull() throws InvalidParkingLevelException {
        ParkingLotFactory parkingLotFactory = new ParkingLotFactory();
        Map<ParkingSpotType, Integer> testSpotAllocations1 = new HashMap<>();
        testSpotAllocations1.put(ParkingSpotType.MOTORCYCLE, 2);
        testSpotAllocations1.put(ParkingSpotType.CAR, 0);
        testSpotAllocations1.put(ParkingSpotType.TRUCK, 2);
        ParkingLot parkingLot = parkingLotFactory.createNewParkingLot(2, ParkingSpotType.values().length, testSpotAllocations1, feeModelList);
        assertThat(parkingLot.getAllLevels().getParkingLevel(0).getTotalNumOfVacantSpots()).isEqualTo(2);
        assertThat(parkingLot.getAllLevels().getParkingLevel(1).getTotalNumOfVacantSpots()).isEqualTo(2);
        assertThat(parkingLot.getAllLevels().getParkingLevel(1).getNumOfVacantSpotsOfType(ParkingSpotType.MOTORCYCLE)).isEqualTo(1);
        assertThat(parkingLot.getAllLevels().getParkingLevel(1).getNumOfVacantSpotsOfType(ParkingSpotType.TRUCK)).isEqualTo(1);
        parkingSpotHelper(11223, VehicleType.MOTORCYCLE, "Shyam", parkingLot);
        parkingSpotHelper(11234, VehicleType.MOTORCYCLE, "Dhone", parkingLot);
        parkingSpotHelper(11225, VehicleType.TRUCK, "Shyam", parkingLot);
        parkingSpotHelper(11226, VehicleType.TRUCK, "Shyam", parkingLot);
        assertThat(parkingLot.getAllLevels().getParkingLevel(0).getTotalNumOfVacantSpots()).isEqualTo(0);
        assertThat(parkingLot.getAllLevels().getParkingLevel(1).getTotalNumOfVacantSpots()).isEqualTo(0);
        assertThat(parkingLot.getAllLevels().getParkingLevel(1).getNumOfVacantSpotsOfType(ParkingSpotType.MOTORCYCLE)).isEqualTo(0);
        assertThat(parkingLot.getAllLevels().getParkingLevel(1).getNumOfVacantSpotsOfType(ParkingSpotType.TRUCK)).isEqualTo(0);
    }

    @DisplayName("Verify Un Parking a Vehicle")
    @Test @Order(8)
    void verifyUnParkingAVechile() throws InvalidParkingLevelException {
        ParkingLotFactory parkingLotFactory = new ParkingLotFactory();
        Map<ParkingSpotType, Integer> testSpotAllocations1 = new HashMap<>();
        testSpotAllocations1.put(ParkingSpotType.MOTORCYCLE, 2);
        testSpotAllocations1.put(ParkingSpotType.CAR, 0);
        testSpotAllocations1.put(ParkingSpotType.TRUCK, 2);
        ParkingLot parkingLot = parkingLotFactory.createNewParkingLot(2, ParkingSpotType.values().length, testSpotAllocations1, feeModelList);
        Vehicle vehicle = new Vehicle(12345, VehicleType.MOTORCYCLE);
        VehicleOwner vehicleOwner = new VehicleOwner("Shyam", vehicle);
        assertThat(parkingLot.getAllLevels().getParkingLevel(0).getTotalNumOfVacantSpots()).isEqualTo(2);
        assertThat(parkingLot.getAllLevels().getParkingLevel(0).getNumOfVacantSpotsOfType(ParkingSpotType.MOTORCYCLE)).isEqualTo(1);
        vehicleOwner.parkVehicle(parkingLot);
        assertThat(parkingLot.getAllLevels().getParkingLevel(0).getTotalNumOfVacantSpots()).isEqualTo(1);
        assertThat(parkingLot.getAllLevels().getParkingLevel(0).getNumOfVacantSpotsOfType(ParkingSpotType.MOTORCYCLE)).isEqualTo(0);
        vehicleOwner.unparkVehicleAndGetAmount(parkingLot);
        assertThat(parkingLot.getAllLevels().getParkingLevel(0).getTotalNumOfVacantSpots()).isEqualTo(2);
        assertThat(parkingLot.getAllLevels().getParkingLevel(0).getNumOfVacantSpotsOfType(ParkingSpotType.MOTORCYCLE)).isEqualTo(1);
    }

    private ParkingSpot parkingSpotHelper(int vehicleId, VehicleType vehicleType,String ownerName, ParkingLot parkingLot) {
        Vehicle vehicle = new Vehicle(vehicleId, vehicleType);
        VehicleOwner vehicleOwner = new VehicleOwner(ownerName, vehicle);
        ParkingTicket parkingTicket = vehicleOwner.parkVehicle(parkingLot);
        System.out.println(parkingTicket);
        return parkingTicket.getParkingSpot();
    }

    private ParkingTicket parkingTicketHelper(int vehicleId, VehicleType vehicleType,String ownerName, ParkingLot parkingLot, String parkingTime) {
        Vehicle vehicle = new Vehicle(vehicleId, vehicleType);
        VehicleOwner vehicleOwner = new VehicleOwner(ownerName, vehicle);
        ParkingTicket parkingTicket = vehicleOwner.parkVehicle(parkingLot, LocalDateTime.parse(parkingTime, formatter));
        System.out.println(parkingTicket);
        return parkingTicket;
    }

}

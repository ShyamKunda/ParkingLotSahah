package Parking.Factories;

import Exceptions.InvalidParkingLevelException;
import Parking.ParkingLevel;
import Parking.ParkingLevelsCollection;
import Parking.ParkingLot;
import Parking.ParkingSpotType;
import Payment.FeeModel;
import Payment.PaymentCriteria;
import Vehicles.VehicleType;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ParkingLotFactoryTest {

    ParkingLotFactory parkingLotFactory;
    Map<ParkingSpotType, Integer> spotAllocations;
    List<FeeModel> feeModelList = new ArrayList<>();


    @BeforeEach
    void setUp() {
        parkingLotFactory = new ParkingLotFactory();
        spotAllocations = new HashMap<>();
        spotAllocations.put(ParkingSpotType.MOTORCYCLE, 5);
        spotAllocations.put(ParkingSpotType.COMPACT, 4);
        spotAllocations.put(ParkingSpotType.LARGE, 3);
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
    }

    /**
     * 1. Create a Parking lot with 2 level and verify levels created
     * 2. Create a Parking lot with 5 level and verify levels created
     */
    @DisplayName("Verify Parking levels Created")
    @Test
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
    @Test
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
    @DisplayName("Verify Total Vacant spots")
    @Test
    void verifyEvenParkingSpots() throws InvalidParkingLevelException {

        Map<ParkingSpotType, Integer> testSpotAllocations = new HashMap<>();
        testSpotAllocations.put(ParkingSpotType.MOTORCYCLE, 8);
        testSpotAllocations.put(ParkingSpotType.COMPACT, 6);
        testSpotAllocations.put(ParkingSpotType.LARGE, 12);
        ParkingLot parkingLot1 = parkingLotFactory.createNewParkingLot(2, ParkingSpotType.values().length, testSpotAllocations, feeModelList);
        ParkingLevel parkingLevel0 =  parkingLot1.getAllLevels().getParkingLevel(0);
        assertThat(parkingLevel0.getTotalNumOfVacantSpots()).isEqualTo(13);
        assertThat(parkingLevel0.getNumOfVacantSpotsOfType(ParkingSpotType.MOTORCYCLE)).isEqualTo(4);
        assertThat(parkingLevel0.getNumOfVacantSpotsOfType(ParkingSpotType.COMPACT)).isEqualTo(3);
        assertThat(parkingLevel0.getNumOfVacantSpotsOfType(ParkingSpotType.LARGE)).isEqualTo(6);
        ParkingLevel parkingLevel1 =  parkingLot1.getAllLevels().getParkingLevel(1);
        assertThat(parkingLevel1.getTotalNumOfVacantSpots()).isEqualTo(13);
        assertThat(parkingLevel1.getNumOfVacantSpotsOfType(ParkingSpotType.MOTORCYCLE)).isEqualTo(4);
        assertThat(parkingLevel1.getNumOfVacantSpotsOfType(ParkingSpotType.COMPACT)).isEqualTo(3);
        assertThat(parkingLevel1.getNumOfVacantSpotsOfType(ParkingSpotType.LARGE)).isEqualTo(6);

        //parkingLevel1.addSpot();
    }

    /**
     * Create a Parking lot with 2 levels
     * Validates Odd number spots
     * Total spots : 27, Motor Cycle spots : 7, Compact Spots : 9, Large spots : 11,
     * Verify level 0 has 12 vacant spot, Motor Cycle spots : 3, Compact Spots : 4, Large spots : 5
     * Verify level 1 has 15 vacant spot, Motor Cycle spots : 4, Compact Spots : 5, Large spots : 6
     */
    @DisplayName("Verify Odd Vacant spots ")
    @Test
    void verifyOddParkingSpots() throws InvalidParkingLevelException {

        Map<ParkingSpotType, Integer> testSpotAllocations = new HashMap<>();
        testSpotAllocations.put(ParkingSpotType.MOTORCYCLE, 7);
        testSpotAllocations.put(ParkingSpotType.COMPACT, 9);
        testSpotAllocations.put(ParkingSpotType.LARGE, 11);
        ParkingLot parkingLot1 = parkingLotFactory.createNewParkingLot(2, ParkingSpotType.values().length, testSpotAllocations, feeModelList);

        ParkingLevel parkingLevel0 =  parkingLot1.getAllLevels().getParkingLevel(0);
        assertThat(parkingLevel0.getTotalNumOfVacantSpots()).isEqualTo(12);
        assertThat(parkingLevel0.getNumOfVacantSpotsOfType(ParkingSpotType.MOTORCYCLE)).isEqualTo(3);
        assertThat(parkingLevel0.getNumOfVacantSpotsOfType(ParkingSpotType.COMPACT)).isEqualTo(4);
        assertThat(parkingLevel0.getNumOfVacantSpotsOfType(ParkingSpotType.LARGE)).isEqualTo(5);
        ParkingLevel parkingLevel1 =  parkingLot1.getAllLevels().getParkingLevel(1);
        assertThat(parkingLevel1.getTotalNumOfVacantSpots()).isEqualTo(15);
        assertThat(parkingLevel1.getNumOfVacantSpotsOfType(ParkingSpotType.MOTORCYCLE)).isEqualTo(4);
        assertThat(parkingLevel1.getNumOfVacantSpotsOfType(ParkingSpotType.COMPACT)).isEqualTo(5);
        assertThat(parkingLevel1.getNumOfVacantSpotsOfType(ParkingSpotType.LARGE)).isEqualTo(6);
    }


}

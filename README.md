# Description

Prerequisites to run : 

    Java : 8
    Maven
    
    Build : mvn clean install
    Run tests : mvn test
    
    Please refer junit tests and write own tests check below instructions

A parking lot is a dedicated area that is intended for parking vehicles. 
Parking lots are present in every city and suburban area. Shopping malls, stadiums, airports, train stations, 
and similar venues often feature a parking lot with a large capacity. 
A parking lot can spread across multiple buildings with multiple floors or can be in a large open area.

The parking lot will allow different types of vehicles to be parked: 

    ○ Motorcycles/Scooters
    ○ Cars/SUVs
    ○ Buses/Trucks

Each vehicle will occupy a single spot and the spot size will be different for different
vehicles.

The number of spots per vehicle type will be different for different parking lots. For
example
*  Motorcycles/scooters: 100 spots
* Cars/SUVs: 80 spots
* Buses/Trucks: 40 spots

When a vehicle is parked, a parking ticket should be generated with the spot number and the entry date-time.

When a vehicle is unparked, a receipt should be generated with the entry date-time, exit date-time, and the applicable fees to be paid.

### How to create Fee Models

    List<FeeModel> airportFeeModel = new ArrayList<>(); 
    FeeModel.Builder feeModelBuilder = FeeModel.Builder.newInstance();
    airportFeeModel.add(feeModelBuilder.setStart(0).setEnd(1).setPrice(0).setVehicleType(VehicleType.MOTORCYCLE)
    .setPaymentCriteria(PaymentCriteria.HOURS).build());
    airportFeeModel.add(feeModelBuilder.setStart(1).setEnd(7).setPrice(40).setVehicleType(VehicleType.MOTORCYCLE)
    .setPaymentCriteria(PaymentCriteria.HOURS).build());
    airportFeeModel.add(new FeeModel(1,7,40, VehicleType.MOTORCYCLE, PaymentCriteria.HOURS));
    airportFeeModel.add(new FeeModel(8,24,60, VehicleType.MOTORCYCLE, PaymentCriteria.HOURS));
    airportFeeModel.add(new FeeModel(24,-1,80, VehicleType.MOTORCYCLE, PaymentCriteria.DAYS));
    airportFeeModel.add(new FeeModel(0,11,60, VehicleType.CAR, PaymentCriteria.HOURS));
    airportFeeModel.add(new FeeModel(12,24,80, VehicleType.CAR, PaymentCriteria.HOURS));

### How to create spot allocations

    Map<ParkingSpotType, Integer> spotAllocations = new HashMap<>();
    spotAllocations.put(ParkingSpotType.MOTORCYCLE, 0);
    spotAllocations.put(ParkingSpotType.CAR, 1);
    spotAllocations.put(ParkingSpotType.TRUCK, 0);

### How to create Parking lot

    //createNewParkingLot(int totalParkinglevels, int parkingSpotTypesCount, Map<ParkingSpotType, Integer> noOfSpotsPerSpotType, List<FeeModel> feeModelList)
    ParkingLot parkingLot = parkingLotFactory.createNewParkingLot(5, ParkingSpotType.values().length, spotAllocations, airportFeeModel);

### How to create a Vehicle and Car?
    Vehicle vehicle1 = new Vehicle(1, VehicleType.CAR);
    VehicleOwner carOwner1 = new VehicleOwner("Chinnu", vehicle1);

### How to create a Vehicle and Owner?
    Vehicle car = new Vehicle(1, VehicleType.CAR);
    VehicleOwner carOwner = new VehicleOwner("Chinnu", vehicle);

### How to park a vehicle?
    Vehicle car = new Vehicle(1, VehicleType.CAR);
    VehicleOwner carOwner = new VehicleOwner("Chinnu", vehicle);

### How to park a vehicle?
    carOwner.parkVehicle(parkingLot, LocalDateTime.parse("29-May-2022 14:04:07", formatter));

    Output : Parking Ticket

### How to unPark a vehicle?
    carOwner.parkVehicle(parkingLot, LocalDateTime.parse("29-May-2022 14:04:07", formatter));

    Output : Payment Ticket Ticket


    

    

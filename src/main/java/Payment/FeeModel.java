package Payment;


import Vehicles.VehicleType;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class FeeModel {

    private int start;
    private int end;
    private int price;
    private VehicleType vehicleType;

    private boolean isFinalCriteria;

    private PaymentCriteria paymentCriteria;

    public FeeModel(Builder builder)
    {
        this.start = builder.start;
        this.end = builder.end;
        this.price = builder.price;
        this.vehicleType = builder.vehicleType;
        this.paymentCriteria = builder.paymentCriteria;

    }
    public static class Builder {
        private int start;
        private int end;
        private int price;
        private VehicleType vehicleType;

        private boolean isFinalCriteria;

        private PaymentCriteria paymentCriteria;

        public static Builder newInstance()
        {
            return new Builder();
        }

        private Builder() {}

        public Builder setStart(int start)
        {
            this.start = start;
            return this;
        }

        public Builder setEnd(int end)
        {
            this.end = end;
            return this;
        }

        public Builder setPrice(int price)
        {
            this.price = price;
            return this;
        }

        public Builder setVehicleType(VehicleType vehicleType)
        {
            this.vehicleType = vehicleType;
            return this;
        }

        public Builder setPaymentCriteria(PaymentCriteria paymentCriteria)
        {
            this.paymentCriteria = paymentCriteria;
            return this;
        }

        public FeeModel build()
        {
            return new FeeModel(this);
        }

    }
    public PaymentCriteria getPaymentCriteria() {
        return paymentCriteria;
    }

    public void setPaymentCriteria(PaymentCriteria paymentCriteria) {
        this.paymentCriteria = paymentCriteria;
    }

    public boolean isIgnorePreviousCriteria() {
        return ignorePreviousCriteria;
    }

    public void setIgnorePreviousCriteria(boolean ignorePreviousCriteria) {
        this.ignorePreviousCriteria = ignorePreviousCriteria;
    }

    private boolean ignorePreviousCriteria = false ;

    public FeeModel(int start, int end, int price, VehicleType vehicleType,
                    PaymentCriteria paymentCriteria, boolean isFinalCriteria) {
        this.start = start;
        this.end = end;
        this.price = price;
        this.vehicleType = vehicleType;
        this.paymentCriteria = paymentCriteria;
        this.isFinalCriteria = isFinalCriteria;
    }

    public FeeModel(int start, int end, int price, VehicleType vehicleType,
                    PaymentCriteria paymentCriteria, boolean isFinalCriteria, boolean ignorePreviousCriteria) {
        this.start = start;
        this.end = end;
        this.price = price;
        this.vehicleType = vehicleType;
        this.paymentCriteria = paymentCriteria;
        this.isFinalCriteria = isFinalCriteria;
        this.ignorePreviousCriteria = ignorePreviousCriteria;
    }

    public FeeModel(int start, int end, int price, VehicleType vehicleType,
                    PaymentCriteria paymentCriteria) {
        this.start = start;
        this.end = end;
        this.price = price;
        this.vehicleType = vehicleType;
        this.paymentCriteria = paymentCriteria;
        this.isFinalCriteria = false;
    }

    public FeeModel(int start, int end, int price, VehicleType vehicleType) {
        this.start = start;
        this.end = end;
        this.price = price;
        this.vehicleType = vehicleType;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }


}

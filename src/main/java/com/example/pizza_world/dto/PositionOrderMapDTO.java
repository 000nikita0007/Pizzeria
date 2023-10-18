package com.example.pizza_world.dto;

import java.util.Objects;

public class PositionOrderMapDTO {

    private int PositionId;

    private int SizeId;

    private int quantity;

    private double price;

    public int getPositionId() {
        return PositionId;
    }

    public void setPositionId(int positionId) {
        PositionId = positionId;
    }

    public int getSizeId() {
        return SizeId;
    }

    public void setSizeId(int sizeId) {
        SizeId = sizeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositionOrderMapDTO that = (PositionOrderMapDTO) o;
        return PositionId == that.PositionId && SizeId == that.SizeId && quantity == that.quantity && Double.compare(that.price, price) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(PositionId, SizeId, quantity, price);
    }

    @Override
    public String toString() {
        return "PositionOrderMapDTO{" +
                "PositionId=" + PositionId +
                ", SizeId=" + SizeId +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}

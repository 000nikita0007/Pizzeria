package com.example.pizza_world.bean;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "position_order_map")
public class PositionOrderMap extends AbstractBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "size_id")
    private Size size;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositionOrderMap that = (PositionOrderMap) o;
        return id == that.id && quantity == that.quantity && Objects.equals(position, that.position) && Objects.equals(order, that.order) && Objects.equals(size, that.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity, position, order, size);
    }

    @Override
    public String toString() {
        return "PositionOrderMap{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", position=" + position +
                ", order=" + order +
                ", size=" + size +
                '}';
    }
}

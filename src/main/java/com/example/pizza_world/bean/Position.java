package com.example.pizza_world.bean;

import com.example.pizza_world.util.YesNoConverter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "position")
public class Position extends AbstractBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    @NotBlank
    @NotNull
    private String name;

    @Column
    @NotBlank
    @NotNull
    @Size(max = 250)
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column (name = "active")
    @Convert(converter = YesNoConverter.class)
    private boolean active;

    @Column
    @NotNull
    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
        Position position = (Position) o;
        return id == position.id && active == position.active && Double.compare(position.price, price) == 0 && Objects.equals(name, position.name) && Objects.equals(description, position.description) && Objects.equals(imageUrl, position.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, imageUrl, active, price);
    }

    @Override
    public String toString() {
        return "Position{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", active=" + active +
                ", price=" + price +
                '}';
    }
}

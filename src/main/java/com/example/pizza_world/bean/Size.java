package com.example.pizza_world.bean;

import com.example.pizza_world.dao.AbstractBeanDao;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "size")
public class Size extends AbstractBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String name;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Size size = (Size) o;
        return id == size.id && Objects.equals(name, size.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Size{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

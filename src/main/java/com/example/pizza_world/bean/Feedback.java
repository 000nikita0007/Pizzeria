package com.example.pizza_world.bean;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "feedback")
public class Feedback extends AbstractBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private int mark;

    @Column
    @NotNull
    @NotBlank
    @Size(max = 250)
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return id == feedback.id && mark == feedback.mark && Objects.equals(text, feedback.text) && Objects.equals(user, feedback.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mark, text, user);
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", mark=" + mark +
                ", text='" + text + '\'' +
                ", user=" + user +
                '}';
    }
}

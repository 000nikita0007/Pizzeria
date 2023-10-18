package com.example.pizza_world.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;

public class UserDTO {

    @NotBlank
    @NotEmpty
    @Size(min = 2,max = 20)
    private String login;

    @NotBlank
    @NotEmpty
    @Size(min = 8,max = 50)
    private String password;

    private String confirmPassword;

    @NotBlank
    @NotEmpty
    private String phone;

    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    public UserDTO() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(login, userDTO.login) && Objects.equals(password, userDTO.password) && Objects.equals(confirmPassword, userDTO.confirmPassword) && Objects.equals(phone, userDTO.phone) && Objects.equals(birthDate, userDTO.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, confirmPassword, phone, birthDate);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", phone='" + phone + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}

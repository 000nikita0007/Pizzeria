package com.example.pizza_world.validator;

import com.example.pizza_world.dto.UserDTO;
import com.example.pizza_world.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO user = (UserDTO) target;

        if (userService.findByLogin(user.getLogin())!=null){
            errors.rejectValue("login", "DUPLICATE","логин занят");
        }
        if (!user.getConfirmPassword().equals(user.getPassword())){
            errors.rejectValue("confirmPassword","DIFFERENT","пароли не совпадают");
        }
        if(!user.getPhone().matches("^\\+375(29|33|25|44)\\d{7}$")){
            errors.rejectValue("phone","dont match","Пожалуйста введите настоящий номер телефона");
        }
        for (int i = 0; i < userService.findAllUsers().size(); i++) {
            if(userService.findAllUsers().get(i).getPhone().equals(user.getPhone()))
                errors.rejectValue("phone","is exist","Номер уже занят");
        }
    }
}

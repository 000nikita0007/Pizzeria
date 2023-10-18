package com.example.pizza_world.service;

import com.example.pizza_world.bean.*;
import com.example.pizza_world.dao.*;
import com.example.pizza_world.dto.PositionOrderMapDTO;
import com.example.pizza_world.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private FeedbackDao feedbackDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private PositionOrderMapDao positionOrderMapDao;

    public void save(UserDTO userDTO) {
        User user = new User();
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(roleDao.findById(2, Role.class));
        user.setLogin(userDTO.getLogin());
        user.setPhone(userDTO.getPhone());
        System.out.println(userDTO.getBirthDate());
        user.setBirthDay(userDTO.getBirthDate());
        userDao.save(user);
    }

    public void delete(User user) {
        List<Feedback> feedbackList = feedbackDao.findAll();
        List<Order> orderList = orderDao.findAll();
        List<PositionOrderMap> positionOrderMapList = positionOrderMapDao.findAll();

        for (int i = 0; i < feedbackList.size(); i++) {
            if (feedbackList.get(i).getUser().equals(user)) {
                feedbackDao.delete(feedbackList.get(i));
            }
        }
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getUser().equals(user)) {
                for (int j = 0; j < positionOrderMapList.size(); j++) {
                    if (positionOrderMapList.get(j).getOrder().equals(orderList.get(i))) {
                        positionOrderMapDao.delete(positionOrderMapList.get(j));
                    }
                }
                orderDao.delete(orderList.get(i));
            }
        }
        userDao.delete(user);
    }

    public void update(User user) {
        userDao.update(user);
    }

    public User findById(int id) {
        return userDao.findById(id, User.class);
    }

    public User findByLogin(String login) {
        return userDao.findByLogin(login);
    }

    public List<User> findAllUsers() {
        return userDao.findAll();
    }

    public void changeUserRole(int userId, int roleId) {
        User user = findById(userId);
        Role role = userDao.findById(roleId, Role.class);

        user.setRole(role);
        update(user);
    }

    public boolean changeUserPassword(User user, String oldPass, String newPass) {
        if (passwordEncoder.matches(oldPass, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPass));
            userDao.update(user);
            return true;
        } else {
            return false;
        }
    }

    public void leaveAnOrder(User user, Feedback feedback) {
        feedback.setUser(user);
        feedbackDao.save(feedback);
    }

    public List<Feedback> findAllFeedback() {
        return feedbackDao.findAll();
    }
}

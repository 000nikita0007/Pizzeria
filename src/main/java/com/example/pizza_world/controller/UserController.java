package com.example.pizza_world.controller;

import com.example.pizza_world.bean.Feedback;
import com.example.pizza_world.bean.Order;
import com.example.pizza_world.bean.User;
import com.example.pizza_world.dto.PositionOrderMapDTO;
import com.example.pizza_world.dto.UserDTO;
import com.example.pizza_world.service.FeedbackService;
import com.example.pizza_world.service.OrderService;
import com.example.pizza_world.service.PositionService;
import com.example.pizza_world.service.UserService;
import com.example.pizza_world.validator.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class UserController {
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private UserValidator userValidator;

    @Autowired
    private UserService userService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/login")
    public String userLogin() {
        return "login";
    }

    @GetMapping("/registration")
    public String userRegistration(ModelMap map) {
        map.put("userDTO", new UserDTO());
        return "registration";
    }

    @PostMapping("/registration")
    public String userRegistration(@ModelAttribute @Valid UserDTO userDTO, BindingResult bindingResult, ModelMap map) {
        userValidator.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            map.put("error", bindingResult);
            return "registration";
        }
        userService.save(userDTO);
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String getHomePage(ModelMap map, Principal principal) {
        map.put("positions", positionService.findAllPositions());
        if (principal != null) {
            map.put("user", userService.findByLogin(principal.getName()));
        }
        return "home";
    }

    @GetMapping("/bucket")
    public String getBucketPage(ModelMap modelMap, Principal principal, HttpSession httpSession) {
        Map<String, PositionOrderMapDTO> cartPositions = (Map<String, PositionOrderMapDTO>) httpSession.getAttribute("cartPositions");

        // Если список не существует, создаем новый
        if (cartPositions == null) {
            cartPositions = new HashMap<>();
        }
        modelMap.put("cartPositions", cartPositions);
        modelMap.put("size", positionService.findSizes());
        modelMap.put("user", userService.findByLogin(principal.getName()));
        modelMap.put("totalPrice", orderService.countTotalPrice(cartPositions));
        return "bucket";
    }

    @PostMapping("/savePositionToCart")
    public String saveToCart(@ModelAttribute PositionOrderMapDTO positionOrderMapDTO, HttpSession httpSession) {

        // Получаем текущий список товаров в корзине из сессии
        Map<String, PositionOrderMapDTO> cartPositions = (Map<String, PositionOrderMapDTO>) httpSession.getAttribute("cartPositions");

        // Если список не существует, создаем новый
        if (cartPositions == null) {
            cartPositions = new HashMap<>();
        }
        if (!cartPositions.containsKey(positionService.findPositionById(positionOrderMapDTO.getPositionId()).getName())) {
            cartPositions.put(positionService.findPositionById(positionOrderMapDTO.getPositionId()).getName(), positionOrderMapDTO);
        }

        httpSession.setAttribute("cartPositions", cartPositions);
        return "redirect:/home";
    }

    @GetMapping("/feedback")
    public String getFeedbackPage(ModelMap map, Principal principal) {
        map.put("user", userService.findByLogin(principal.getName()));
        List<Feedback> feedbackList = userService.findAllFeedback();
        Collections.reverse(feedbackList);
        map.put("feedbackList", feedbackList);
        return "feedback";
    }

    @PostMapping("/updatePositionOrderMap")
    public String updateCartPosition(@ModelAttribute PositionOrderMapDTO positionOrderMapDTO, HttpSession httpSession) {
        System.out.println(positionOrderMapDTO);
        Map<String, PositionOrderMapDTO> cartPositions = (Map<String, PositionOrderMapDTO>) httpSession.getAttribute("cartPositions");

        cartPositions.replace(positionService.findPositionById(positionOrderMapDTO.getPositionId()).getName(),
                orderService.setPrice(positionOrderMapDTO));
        return "redirect:/bucket";
    }

    @GetMapping("/reducePositionQuantity")
    public String reducePositionQuantity(@RequestParam("id") int positionId, HttpSession httpSession) {
        Map<String, PositionOrderMapDTO> cartPositions = (Map<String, PositionOrderMapDTO>) httpSession.getAttribute("cartPositions");
        String deleteItem = null;

        for (Map.Entry<String, PositionOrderMapDTO> entry : cartPositions.entrySet()) {
            String key = entry.getKey();
            PositionOrderMapDTO value = entry.getValue();
            if (value.getPositionId() == positionId) {
                if (value.getQuantity() == 1) {
                    deleteItem = key;
                } else {
                    value.setQuantity(value.getQuantity() - 1);
                }
                value = orderService.setPrice(value);
            }
        }
        if (deleteItem != null) {
            cartPositions.remove(deleteItem);
        }
        return "redirect:/bucket";
    }

    @GetMapping("/increasePositionQuantity")
    public String increasePositionQuantity(@RequestParam("id") int positionId, HttpSession httpSession) {
        Map<String, PositionOrderMapDTO> cartPositions = (Map<String, PositionOrderMapDTO>) httpSession.getAttribute("cartPositions");

        for (Map.Entry<String, PositionOrderMapDTO> entry : cartPositions.entrySet()) {
            PositionOrderMapDTO value = entry.getValue();
            if (value.getPositionId() == positionId) {
                value.setQuantity(value.getQuantity() + 1);
                value = orderService.setPrice(value);
            }
        }
        return "redirect:bucket";
    }

    @PostMapping("/placeAnOrder")
    public String placeAnOrder(@ModelAttribute @Valid Order order,BindingResult bindingResult, HttpSession httpSession, Principal principal,
                               RedirectAttributes redirectAttributes) {
        Map<String, PositionOrderMapDTO> cartPositions = (Map<String, PositionOrderMapDTO>) httpSession.getAttribute("cartPositions");
        order.setUser(userService.findByLogin(principal.getName()));
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/bucket";
        }
        if (cartPositions == null || cartPositions.isEmpty()) {
            redirectAttributes.addFlashAttribute("failure", "Корзина пуста");
            return "redirect:/bucket";
        }
        int orderId = orderService.saveOrder(order, cartPositions).getId();
        httpSession.removeAttribute("cartPositions");
        return "redirect:/orderPage?orderId=" + orderId;
    }

    @GetMapping("/orderPage")
    public String getOrderPage(@RequestParam("orderId") int orderId, ModelMap map, Principal principal) {
        map.put("user", userService.findByLogin(principal.getName()));
        map.put("order", orderService.findOrderById(orderId));
        map.put("orderPositions", orderService.findOrderPositions(orderService.findOrderById(orderId)));
        return "order";
    }

    @GetMapping("/personalPage")
    public String getPersonalPage(ModelMap map, Principal principal) {
        map.put("user", userService.findByLogin(principal.getName()));
        List<Order> orders = orderService.findUserOrders(userService.findByLogin(principal.getName()));
        Collections.reverse(orders);
        map.put("userOrders", orders);
        return "personalPage";
    }

    @PostMapping("/userUpdate")
    public String updateUserInfo(@ModelAttribute @Valid User user, BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes, Principal principal) {
        User updatedUser = userService.findByLogin(principal.getName());
        if (!user.getPhone().matches("^\\+375(29|33|25|44)\\d{7}$")) {
            redirectAttributes.addFlashAttribute("error", "введите корректный номер телефона");
            return "redirect:/personalPage";
        }
        for (int i = 0; i < userService.findAllUsers().size(); i++) {
            if (userService.findAllUsers().get(i).getPhone().equals(user.getPhone())) {
                redirectAttributes.addFlashAttribute("error", "номер уже занят");
                return "redirect:/personalPage";
            }
        }
        System.out.println(user.getLogin());
        System.out.println(user.getPhone());
        updatedUser.setPhone(user.getPhone());
        updatedUser.setBirthDay(user.getBirthDay());
        updatedUser.setLogin(user.getLogin());

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/personalPage";
        }
        userService.update(updatedUser);
        return "redirect:/personalPage";
    }

    @PostMapping("/changePassword")
    public String changeUserPassword(@RequestParam("oldPassword") String oldPass,
                                     @RequestParam("newPassword") String newPass, Principal principal,
                                     RedirectAttributes redirectAttributes) {
        if (newPass.length() < 8) {
            redirectAttributes.addFlashAttribute("failure", "Длина пароля должна быть больше 8" +
                    "символов");
            return "redirect:/personalPage";
        }
        if (newPass.isBlank()) {
            redirectAttributes.addFlashAttribute("failure", "Пароль не должен быть пустым");
            return "redirect:/personalPage";
        }
        if (userService.changeUserPassword(userService.findByLogin(principal.getName()), oldPass, newPass)) {
            redirectAttributes.addFlashAttribute("success", "Пароль изменен");
            return "redirect:/personalPage";
        } else {
            redirectAttributes.addFlashAttribute("failure", "Неверный пароль");
            return "redirect:/personalPage";
        }
    }

    @PostMapping("/leaveAnOrder")
    public String leaveAnOrder(@ModelAttribute @Valid Feedback feedback, BindingResult bindingResult,
                               Principal principal, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/feedback";
        } else {
            userService.leaveAnOrder(userService.findByLogin(principal.getName()), feedback);
            return "redirect:/feedback";
        }
    }

    @GetMapping("/orderCancel")
    public String cancelOrder(@RequestParam("id") int id) {
        Order order = orderService.findOrderById(id);
        orderService.cancelOrder(order);
        return "redirect:/personalPage";
    }

    @PostMapping("/submitOrder")
    public String submitOrder(@RequestParam("address") String address, @RequestParam("orderId") int orderId,
                              RedirectAttributes redirectAttributes) {
        if (address.isBlank()) {
            redirectAttributes.addFlashAttribute("error","Адресс не должен быть пустым");
            return "redirect:/orderPage?orderId="+orderId;
        }
        orderService.submitOrder(orderService.findOrderById(orderId), address);
        return "redirect:/personalPage";
    }
    @GetMapping("/deleteFeedback")
    public String deleteFeedback(@RequestParam("id") int feedbackId) {
        feedbackService.deleteFeedback(feedbackId);
        return "redirect:/feedback";
    }
}

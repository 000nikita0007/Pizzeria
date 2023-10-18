package com.example.pizza_world.controller;

import com.example.pizza_world.bean.Order;
import com.example.pizza_world.bean.Position;
import com.example.pizza_world.bean.User;
import com.example.pizza_world.service.FeedbackService;
import com.example.pizza_world.service.OrderService;
import com.example.pizza_world.service.PositionService;
import com.example.pizza_world.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping
    public String getAdminPage(ModelMap map, Principal principal) {
        map.put("user", userService.findByLogin(principal.getName()));
        List<Order> orders = orderService.findAll();
        Collections.reverse(orders);
        map.put("orders", orders);
        map.put("positions", positionService.findAllPositions());
        map.put("users", userService.findAllUsers());
        return "admin";
    }

    @GetMapping("/userEdit")
    public String getUserEditPage(@RequestParam("id") int id, ModelMap map, Principal principal) {
        map.put("user", userService.findByLogin(principal.getName()));
        map.put("updateUser", userService.findById(id));
        return "userEdit";
    }

    @PostMapping("/userEdit")
    public String userEdit(@RequestParam("userId") int userId, @RequestParam("roleId") int roleId) {
        userService.changeUserRole(userId, roleId);
        return "redirect:/admin";
    }

    @GetMapping("/userDelete")
    public String deleteUser(@RequestParam("id") int id) {
        User user = userService.findById(id);
        userService.delete(user);
        return "redirect:/admin";
    }

    @PostMapping("/addPosition")
    public String addPosition(@ModelAttribute @Valid Position position, BindingResult bindingResult,
                              @RequestParam("imageFile") MultipartFile multipartFile,
                              RedirectAttributes redirectAttributes) {
        System.out.println(position.getPrice());
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/admin";
        }
        if (!multipartFile.isEmpty()) {
            String imageUrl = positionService.uploadImage(multipartFile); // Загрузите изображение и получите URL
            position.setImageUrl(imageUrl);
        }
        positionService.savePosition(position);
        return "redirect:/admin";
    }

    @GetMapping("/positionEdit")
    public String getPositionEditPage(@RequestParam("id") int id, ModelMap map, Principal principal) {
        map.put("user", userService.findByLogin(principal.getName()));
        map.put("position", positionService.findPositionById(id));
        return "positionEdit";
    }

    @PostMapping("/editPosition")
    public String positionEdit(@ModelAttribute Position position,
                               @RequestParam("imageFile") MultipartFile multipartFile ) {
        if (!multipartFile.isEmpty()) {
            String imageUrl = positionService.uploadImage(multipartFile); // Загрузите изображение и получите URL
            position.setImageUrl(imageUrl);
        }
        positionService.updatePosition(position);
        return "redirect:/admin";
    }

    @GetMapping("/positionDelete")
    public String deletePosition(@RequestParam("id") int id) {
        Position position = positionService.findPositionById(id);
        positionService.deletePosition(position);
        return "redirect:/admin";
    }

    @GetMapping("/orderEdit")
    public String getOrderEditPage(@RequestParam("id") int id, ModelMap modelMap, Principal principal) {
        modelMap.put("user", userService.findByLogin(principal.getName()));
        modelMap.put("order", orderService.findOrderById(id));
        modelMap.put("orderPositions", orderService.findOrderPositions(orderService.findOrderById(id)));
        modelMap.put("orderStatuses",orderService.findAllStatuses());
        return "adminOrderPage";
    }

    @PostMapping("/processOrder")
    public String processOrder(@RequestParam("statusId") int statusId, @RequestParam("orderId") int orderId) {
        Order order = orderService.findOrderById(orderId);
        orderService.processOrder(order,statusId);
        return "redirect:/admin/orderEdit?id=" + orderId;
    }

    @GetMapping("/deleteFeedback")
    public String deleteFeedback(@RequestParam("id") int feedbackId) {
        feedbackService.deleteFeedback(feedbackId);
        return "redirect:/feedback";
    }
}

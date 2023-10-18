package com.example.pizza_world.service;

import com.example.pizza_world.bean.*;
import com.example.pizza_world.dao.OrderDao;
import com.example.pizza_world.dao.OrderStatusDao;
import com.example.pizza_world.dao.PositionOrderMapDao;
import com.example.pizza_world.dao.SizeDao;
import com.example.pizza_world.dto.PositionOrderMapDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private PositionService positionService;

    @Autowired
    private OrderStatusDao orderStatusDao;

    @Autowired
    private PositionOrderMapDao positionOrderMapDao;

    @Autowired
    private SizeDao sizeDao;

    public List<Order> findAll() {
        return orderDao.findAll();
    }

    public PositionOrderMapDTO setPrice(PositionOrderMapDTO positionOrderMapDTO) {
        double[] array = {1, 1.5, 2};

        if ((positionOrderMapDTO.getPrice() / positionOrderMapDTO.getQuantity()) !=
                        Math.round(positionService.findPositionById(positionOrderMapDTO
                                .getPositionId()).getPrice() * array[positionOrderMapDTO.getSizeId() - 1] * 100.0) / 100.0) {
            positionOrderMapDTO.setPrice(Math.round(positionService.findPositionById(positionOrderMapDTO
                    .getPositionId()).getPrice() * array[positionOrderMapDTO.getSizeId() - 1] * 100.0) / 100.0);
        }
        positionOrderMapDTO.setPrice(positionOrderMapDTO.getPrice() * positionOrderMapDTO.getQuantity());
        return positionOrderMapDTO;
    }

    public double countTotalPrice(Map<String, PositionOrderMapDTO> map) {
        double totalPrice = 0;

        for (Map.Entry<String, PositionOrderMapDTO> entry : map.entrySet()) {
            PositionOrderMapDTO value = entry.getValue();
            totalPrice += value.getPrice();
        }
        return totalPrice;
    }

    public Order saveOrder(Order order, Map<String, PositionOrderMapDTO> cartPositions) {
        OrderStatus orderStatus = orderStatusDao.findById(1, OrderStatus.class);
        order.setOrderStatus(orderStatus);
        order.setCreationDate(new Date());
        orderDao.save(order);

        for (Map.Entry<String, PositionOrderMapDTO> entry : cartPositions.entrySet()) {
            PositionOrderMapDTO value = entry.getValue();
            PositionOrderMap positionOrderMap = new PositionOrderMap();
            positionOrderMap.setOrder(order);
            positionOrderMap.setPosition(positionService.findPositionById(value.getPositionId()));
            positionOrderMap.setQuantity(value.getQuantity());
            positionOrderMap.setSize(sizeDao.findById(value.getSizeId(), Size.class));
            positionOrderMapDao.save(positionOrderMap);
        }
        return order;
    }

    public void cancelOrder(Order order) {
        OrderStatus orderStatus = orderStatusDao.findById(4, OrderStatus.class);
        order.setOrderStatus(orderStatus);
        orderDao.update(order);
    }

    public Order findOrderById(int id) {
        return orderDao.findById(id, Order.class);
    }

    public List<PositionOrderMap> findOrderPositions(Order order) {
       return positionOrderMapDao.findOrderPositions(order);
    }

    public List<Order> findUserOrders(User user) {
        return orderDao.findUserOrders(user);
    }

    public void processOrder(Order order, int statusId) {
        OrderStatus orderStatus = orderStatusDao.findById(statusId, OrderStatus.class);
        order.setOrderStatus(orderStatus);
        orderDao.update(order);
    }
    
    public List<OrderStatus> findAllStatuses() {
        return orderStatusDao.findAll();
    }

    public void submitOrder(Order order, String address) {
        order.setAddress(address);
        orderDao.update(order);
    }
}
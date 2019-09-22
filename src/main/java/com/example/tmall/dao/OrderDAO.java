package com.example.tmall.dao;

import com.example.tmall.pojo.Order;
import com.example.tmall.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDAO extends JpaRepository<Order, Integer>{
    public List<Order> findByUserAndStatusNotOrderByIdDesc(User user, String status);
}

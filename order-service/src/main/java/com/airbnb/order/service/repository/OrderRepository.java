package com.airbnb.order.service.repository;

import com.airbnb.order.service.entity.Order;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CassandraRepository<Order, Integer> {
    @AllowFiltering
    List<Order> findAllByUserId(int id);
}

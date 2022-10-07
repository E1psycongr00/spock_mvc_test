package com.spock.test_demo.repository;

import com.spock.test_demo.entity.OrderEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    OrderEntity findByMemberEntity_Email(String email);

}
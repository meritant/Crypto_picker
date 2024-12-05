package com.example.cryptopicker.repository;

import com.example.cryptopicker.model.Alert;
import com.example.cryptopicker.model.AlertStatus;
import com.example.cryptopicker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByStatus(AlertStatus status);
    List<Alert> findByUserOrderByIdDesc(User user);
}
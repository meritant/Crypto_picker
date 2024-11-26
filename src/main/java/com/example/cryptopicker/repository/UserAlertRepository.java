package com.example.cryptopicker.repository;

import com.example.cryptopicker.model.UserAlert;
import com.example.cryptopicker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserAlertRepository extends JpaRepository<UserAlert, Long> {
    List<UserAlert> findByUser(User user);
    List<UserAlert> findByCryptoSymbol(String cryptoSymbol);
}
package com.example.cryptopicker.repository;

import com.example.cryptopicker.model.UserCryptoSelection;
import com.example.cryptopicker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserCryptoSelectionRepository extends JpaRepository<UserCryptoSelection, Long> {
    Optional<UserCryptoSelection> findByUser(User user);
}
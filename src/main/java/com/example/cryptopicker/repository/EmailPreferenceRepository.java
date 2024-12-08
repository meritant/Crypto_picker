package com.example.cryptopicker.repository;

import com.example.cryptopicker.model.EmailPreference;
import com.example.cryptopicker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmailPreferenceRepository extends JpaRepository<EmailPreference, Long> {
    Optional<EmailPreference> findByUser(User user);
    
    List<EmailPreference> findByDailyReportEnabledTrueAndReportTime(LocalTime reportTime);
}
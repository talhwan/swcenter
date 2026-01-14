package com.thc.sprbasic2025.repository;

import com.thc.sprbasic2025.domain.Popup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PopupRepository extends JpaRepository<Popup, Long> {
    Popup findBySequence(Integer sequence);
}

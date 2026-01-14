package com.thc.sprbasic2025.repository;

import com.thc.sprbasic2025.domain.Faq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Long> {
    Faq findBySequence(Integer sequence);
}

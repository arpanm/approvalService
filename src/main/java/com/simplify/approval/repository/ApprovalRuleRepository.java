package com.simplify.approval.repository;

import com.simplify.approval.domain.ApprovalRule;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ApprovalRule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApprovalRuleRepository extends JpaRepository<ApprovalRule, Long> {}

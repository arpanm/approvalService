package com.simplify.approval.repository;

import com.simplify.approval.domain.ApprovalLevelStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ApprovalLevelStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApprovalLevelStatusRepository extends JpaRepository<ApprovalLevelStatus, Long> {}

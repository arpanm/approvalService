package com.simplify.approval.repository;

import com.simplify.approval.domain.ApprovalRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ApprovalRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequest, Long> {}

package com.simplify.approval.repository;

import com.simplify.approval.domain.ApprovalRequestItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ApprovalRequestItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApprovalRequestItemRepository extends JpaRepository<ApprovalRequestItem, Long> {}

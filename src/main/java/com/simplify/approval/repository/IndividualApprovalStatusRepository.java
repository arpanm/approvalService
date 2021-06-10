package com.simplify.approval.repository;

import com.simplify.approval.domain.IndividualApprovalStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the IndividualApprovalStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndividualApprovalStatusRepository extends JpaRepository<IndividualApprovalStatus, Long> {}

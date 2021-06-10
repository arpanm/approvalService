package com.simplify.approval.repository;

import com.simplify.approval.domain.Approver;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Approver entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApproverRepository extends JpaRepository<Approver, Long> {}

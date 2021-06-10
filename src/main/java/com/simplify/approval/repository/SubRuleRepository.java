package com.simplify.approval.repository;

import com.simplify.approval.domain.SubRule;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SubRule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubRuleRepository extends JpaRepository<SubRule, Long> {}

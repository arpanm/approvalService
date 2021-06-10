package com.simplify.approval.repository;

import com.simplify.approval.domain.SubRuleInListItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SubRuleInListItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubRuleInListItemRepository extends JpaRepository<SubRuleInListItem, Long> {}

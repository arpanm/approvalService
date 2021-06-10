package com.simplify.approval.service;

import com.simplify.approval.domain.ApprovalRule;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ApprovalRule}.
 */
public interface ApprovalRuleService {
    /**
     * Save a approvalRule.
     *
     * @param approvalRule the entity to save.
     * @return the persisted entity.
     */
    ApprovalRule save(ApprovalRule approvalRule);

    /**
     * Partially updates a approvalRule.
     *
     * @param approvalRule the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ApprovalRule> partialUpdate(ApprovalRule approvalRule);

    /**
     * Get all the approvalRules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ApprovalRule> findAll(Pageable pageable);

    /**
     * Get the "id" approvalRule.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ApprovalRule> findOne(Long id);

    /**
     * Delete the "id" approvalRule.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

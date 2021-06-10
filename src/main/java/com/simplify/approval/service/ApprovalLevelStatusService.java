package com.simplify.approval.service;

import com.simplify.approval.domain.ApprovalLevelStatus;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ApprovalLevelStatus}.
 */
public interface ApprovalLevelStatusService {
    /**
     * Save a approvalLevelStatus.
     *
     * @param approvalLevelStatus the entity to save.
     * @return the persisted entity.
     */
    ApprovalLevelStatus save(ApprovalLevelStatus approvalLevelStatus);

    /**
     * Partially updates a approvalLevelStatus.
     *
     * @param approvalLevelStatus the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ApprovalLevelStatus> partialUpdate(ApprovalLevelStatus approvalLevelStatus);

    /**
     * Get all the approvalLevelStatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ApprovalLevelStatus> findAll(Pageable pageable);

    /**
     * Get the "id" approvalLevelStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ApprovalLevelStatus> findOne(Long id);

    /**
     * Delete the "id" approvalLevelStatus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

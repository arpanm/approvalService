package com.simplify.approval.service;

import com.simplify.approval.domain.IndividualApprovalStatus;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link IndividualApprovalStatus}.
 */
public interface IndividualApprovalStatusService {
    /**
     * Save a individualApprovalStatus.
     *
     * @param individualApprovalStatus the entity to save.
     * @return the persisted entity.
     */
    IndividualApprovalStatus save(IndividualApprovalStatus individualApprovalStatus);

    /**
     * Partially updates a individualApprovalStatus.
     *
     * @param individualApprovalStatus the entity to update partially.
     * @return the persisted entity.
     */
    Optional<IndividualApprovalStatus> partialUpdate(IndividualApprovalStatus individualApprovalStatus);

    /**
     * Get all the individualApprovalStatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IndividualApprovalStatus> findAll(Pageable pageable);

    /**
     * Get the "id" individualApprovalStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IndividualApprovalStatus> findOne(Long id);

    /**
     * Delete the "id" individualApprovalStatus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

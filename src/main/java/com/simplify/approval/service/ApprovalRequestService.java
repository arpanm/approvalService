package com.simplify.approval.service;

import com.simplify.approval.domain.ApprovalRequest;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ApprovalRequest}.
 */
public interface ApprovalRequestService {
    /**
     * Save a approvalRequest.
     *
     * @param approvalRequest the entity to save.
     * @return the persisted entity.
     */
    ApprovalRequest save(ApprovalRequest approvalRequest);

    /**
     * Partially updates a approvalRequest.
     *
     * @param approvalRequest the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ApprovalRequest> partialUpdate(ApprovalRequest approvalRequest);

    /**
     * Get all the approvalRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ApprovalRequest> findAll(Pageable pageable);

    /**
     * Get the "id" approvalRequest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ApprovalRequest> findOne(Long id);

    /**
     * Delete the "id" approvalRequest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

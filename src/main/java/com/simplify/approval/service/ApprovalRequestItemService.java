package com.simplify.approval.service;

import com.simplify.approval.domain.ApprovalRequestItem;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ApprovalRequestItem}.
 */
public interface ApprovalRequestItemService {
    /**
     * Save a approvalRequestItem.
     *
     * @param approvalRequestItem the entity to save.
     * @return the persisted entity.
     */
    ApprovalRequestItem save(ApprovalRequestItem approvalRequestItem);

    /**
     * Partially updates a approvalRequestItem.
     *
     * @param approvalRequestItem the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ApprovalRequestItem> partialUpdate(ApprovalRequestItem approvalRequestItem);

    /**
     * Get all the approvalRequestItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ApprovalRequestItem> findAll(Pageable pageable);

    /**
     * Get the "id" approvalRequestItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ApprovalRequestItem> findOne(Long id);

    /**
     * Delete the "id" approvalRequestItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

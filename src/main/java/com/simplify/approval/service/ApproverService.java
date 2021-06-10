package com.simplify.approval.service;

import com.simplify.approval.domain.Approver;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Approver}.
 */
public interface ApproverService {
    /**
     * Save a approver.
     *
     * @param approver the entity to save.
     * @return the persisted entity.
     */
    Approver save(Approver approver);

    /**
     * Partially updates a approver.
     *
     * @param approver the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Approver> partialUpdate(Approver approver);

    /**
     * Get all the approvers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Approver> findAll(Pageable pageable);

    /**
     * Get the "id" approver.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Approver> findOne(Long id);

    /**
     * Delete the "id" approver.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

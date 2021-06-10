package com.simplify.approval.service;

import com.simplify.approval.domain.SubRule;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link SubRule}.
 */
public interface SubRuleService {
    /**
     * Save a subRule.
     *
     * @param subRule the entity to save.
     * @return the persisted entity.
     */
    SubRule save(SubRule subRule);

    /**
     * Partially updates a subRule.
     *
     * @param subRule the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SubRule> partialUpdate(SubRule subRule);

    /**
     * Get all the subRules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubRule> findAll(Pageable pageable);

    /**
     * Get the "id" subRule.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubRule> findOne(Long id);

    /**
     * Delete the "id" subRule.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

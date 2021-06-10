package com.simplify.approval.service;

import com.simplify.approval.domain.SubRuleInListItem;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link SubRuleInListItem}.
 */
public interface SubRuleInListItemService {
    /**
     * Save a subRuleInListItem.
     *
     * @param subRuleInListItem the entity to save.
     * @return the persisted entity.
     */
    SubRuleInListItem save(SubRuleInListItem subRuleInListItem);

    /**
     * Partially updates a subRuleInListItem.
     *
     * @param subRuleInListItem the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SubRuleInListItem> partialUpdate(SubRuleInListItem subRuleInListItem);

    /**
     * Get all the subRuleInListItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubRuleInListItem> findAll(Pageable pageable);

    /**
     * Get the "id" subRuleInListItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubRuleInListItem> findOne(Long id);

    /**
     * Delete the "id" subRuleInListItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

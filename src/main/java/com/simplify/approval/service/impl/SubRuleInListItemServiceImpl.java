package com.simplify.approval.service.impl;

import com.simplify.approval.domain.SubRuleInListItem;
import com.simplify.approval.repository.SubRuleInListItemRepository;
import com.simplify.approval.service.SubRuleInListItemService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SubRuleInListItem}.
 */
@Service
@Transactional
public class SubRuleInListItemServiceImpl implements SubRuleInListItemService {

    private final Logger log = LoggerFactory.getLogger(SubRuleInListItemServiceImpl.class);

    private final SubRuleInListItemRepository subRuleInListItemRepository;

    public SubRuleInListItemServiceImpl(SubRuleInListItemRepository subRuleInListItemRepository) {
        this.subRuleInListItemRepository = subRuleInListItemRepository;
    }

    @Override
    public SubRuleInListItem save(SubRuleInListItem subRuleInListItem) {
        log.debug("Request to save SubRuleInListItem : {}", subRuleInListItem);
        return subRuleInListItemRepository.save(subRuleInListItem);
    }

    @Override
    public Optional<SubRuleInListItem> partialUpdate(SubRuleInListItem subRuleInListItem) {
        log.debug("Request to partially update SubRuleInListItem : {}", subRuleInListItem);

        return subRuleInListItemRepository
            .findById(subRuleInListItem.getId())
            .map(
                existingSubRuleInListItem -> {
                    if (subRuleInListItem.getStrItem() != null) {
                        existingSubRuleInListItem.setStrItem(subRuleInListItem.getStrItem());
                    }
                    if (subRuleInListItem.getDecItem() != null) {
                        existingSubRuleInListItem.setDecItem(subRuleInListItem.getDecItem());
                    }

                    return existingSubRuleInListItem;
                }
            )
            .map(subRuleInListItemRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubRuleInListItem> findAll(Pageable pageable) {
        log.debug("Request to get all SubRuleInListItems");
        return subRuleInListItemRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubRuleInListItem> findOne(Long id) {
        log.debug("Request to get SubRuleInListItem : {}", id);
        return subRuleInListItemRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubRuleInListItem : {}", id);
        subRuleInListItemRepository.deleteById(id);
    }
}

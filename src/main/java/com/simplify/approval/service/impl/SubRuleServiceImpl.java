package com.simplify.approval.service.impl;

import com.simplify.approval.domain.SubRule;
import com.simplify.approval.repository.SubRuleRepository;
import com.simplify.approval.service.SubRuleService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SubRule}.
 */
@Service
@Transactional
public class SubRuleServiceImpl implements SubRuleService {

    private final Logger log = LoggerFactory.getLogger(SubRuleServiceImpl.class);

    private final SubRuleRepository subRuleRepository;

    public SubRuleServiceImpl(SubRuleRepository subRuleRepository) {
        this.subRuleRepository = subRuleRepository;
    }

    @Override
    public SubRule save(SubRule subRule) {
        log.debug("Request to save SubRule : {}", subRule);
        return subRuleRepository.save(subRule);
    }

    @Override
    public Optional<SubRule> partialUpdate(SubRule subRule) {
        log.debug("Request to partially update SubRule : {}", subRule);

        return subRuleRepository
            .findById(subRule.getId())
            .map(
                existingSubRule -> {
                    if (subRule.getFieldName() != null) {
                        existingSubRule.setFieldName(subRule.getFieldName());
                    }
                    if (subRule.getDataType() != null) {
                        existingSubRule.setDataType(subRule.getDataType());
                    }
                    if (subRule.getCondition() != null) {
                        existingSubRule.setCondition(subRule.getCondition());
                    }
                    if (subRule.getRangeMinValue() != null) {
                        existingSubRule.setRangeMinValue(subRule.getRangeMinValue());
                    }
                    if (subRule.getRangeMaxValue() != null) {
                        existingSubRule.setRangeMaxValue(subRule.getRangeMaxValue());
                    }
                    if (subRule.getEqualStrValue() != null) {
                        existingSubRule.setEqualStrValue(subRule.getEqualStrValue());
                    }
                    if (subRule.getEqualDecValue() != null) {
                        existingSubRule.setEqualDecValue(subRule.getEqualDecValue());
                    }
                    if (subRule.getAppendType() != null) {
                        existingSubRule.setAppendType(subRule.getAppendType());
                    }

                    return existingSubRule;
                }
            )
            .map(subRuleRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubRule> findAll(Pageable pageable) {
        log.debug("Request to get all SubRules");
        return subRuleRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubRule> findOne(Long id) {
        log.debug("Request to get SubRule : {}", id);
        return subRuleRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubRule : {}", id);
        subRuleRepository.deleteById(id);
    }
}

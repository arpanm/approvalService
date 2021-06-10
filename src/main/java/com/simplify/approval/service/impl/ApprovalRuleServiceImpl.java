package com.simplify.approval.service.impl;

import com.simplify.approval.domain.ApprovalRule;
import com.simplify.approval.repository.ApprovalRuleRepository;
import com.simplify.approval.service.ApprovalRuleService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ApprovalRule}.
 */
@Service
@Transactional
public class ApprovalRuleServiceImpl implements ApprovalRuleService {

    private final Logger log = LoggerFactory.getLogger(ApprovalRuleServiceImpl.class);

    private final ApprovalRuleRepository approvalRuleRepository;

    public ApprovalRuleServiceImpl(ApprovalRuleRepository approvalRuleRepository) {
        this.approvalRuleRepository = approvalRuleRepository;
    }

    @Override
    public ApprovalRule save(ApprovalRule approvalRule) {
        log.debug("Request to save ApprovalRule : {}", approvalRule);
        return approvalRuleRepository.save(approvalRule);
    }

    @Override
    public Optional<ApprovalRule> partialUpdate(ApprovalRule approvalRule) {
        log.debug("Request to partially update ApprovalRule : {}", approvalRule);

        return approvalRuleRepository
            .findById(approvalRule.getId())
            .map(
                existingApprovalRule -> {
                    if (approvalRule.getProgramId() != null) {
                        existingApprovalRule.setProgramId(approvalRule.getProgramId());
                    }
                    if (approvalRule.getType() != null) {
                        existingApprovalRule.setType(approvalRule.getType());
                    }
                    if (approvalRule.getCreatedBy() != null) {
                        existingApprovalRule.setCreatedBy(approvalRule.getCreatedBy());
                    }
                    if (approvalRule.getCreatedAt() != null) {
                        existingApprovalRule.setCreatedAt(approvalRule.getCreatedAt());
                    }
                    if (approvalRule.getUpdatedBy() != null) {
                        existingApprovalRule.setUpdatedBy(approvalRule.getUpdatedBy());
                    }
                    if (approvalRule.getUpdatedAt() != null) {
                        existingApprovalRule.setUpdatedAt(approvalRule.getUpdatedAt());
                    }

                    return existingApprovalRule;
                }
            )
            .map(approvalRuleRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApprovalRule> findAll(Pageable pageable) {
        log.debug("Request to get all ApprovalRules");
        return approvalRuleRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ApprovalRule> findOne(Long id) {
        log.debug("Request to get ApprovalRule : {}", id);
        return approvalRuleRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ApprovalRule : {}", id);
        approvalRuleRepository.deleteById(id);
    }
}

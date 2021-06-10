package com.simplify.approval.service.impl;

import com.simplify.approval.domain.ApprovalLevelStatus;
import com.simplify.approval.repository.ApprovalLevelStatusRepository;
import com.simplify.approval.service.ApprovalLevelStatusService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ApprovalLevelStatus}.
 */
@Service
@Transactional
public class ApprovalLevelStatusServiceImpl implements ApprovalLevelStatusService {

    private final Logger log = LoggerFactory.getLogger(ApprovalLevelStatusServiceImpl.class);

    private final ApprovalLevelStatusRepository approvalLevelStatusRepository;

    public ApprovalLevelStatusServiceImpl(ApprovalLevelStatusRepository approvalLevelStatusRepository) {
        this.approvalLevelStatusRepository = approvalLevelStatusRepository;
    }

    @Override
    public ApprovalLevelStatus save(ApprovalLevelStatus approvalLevelStatus) {
        log.debug("Request to save ApprovalLevelStatus : {}", approvalLevelStatus);
        return approvalLevelStatusRepository.save(approvalLevelStatus);
    }

    @Override
    public Optional<ApprovalLevelStatus> partialUpdate(ApprovalLevelStatus approvalLevelStatus) {
        log.debug("Request to partially update ApprovalLevelStatus : {}", approvalLevelStatus);

        return approvalLevelStatusRepository
            .findById(approvalLevelStatus.getId())
            .map(
                existingApprovalLevelStatus -> {
                    if (approvalLevelStatus.getStatus() != null) {
                        existingApprovalLevelStatus.setStatus(approvalLevelStatus.getStatus());
                    }
                    if (approvalLevelStatus.getLevel() != null) {
                        existingApprovalLevelStatus.setLevel(approvalLevelStatus.getLevel());
                    }
                    if (approvalLevelStatus.getClientTime() != null) {
                        existingApprovalLevelStatus.setClientTime(approvalLevelStatus.getClientTime());
                    }
                    if (approvalLevelStatus.getCreatedBy() != null) {
                        existingApprovalLevelStatus.setCreatedBy(approvalLevelStatus.getCreatedBy());
                    }
                    if (approvalLevelStatus.getCreatedAt() != null) {
                        existingApprovalLevelStatus.setCreatedAt(approvalLevelStatus.getCreatedAt());
                    }
                    if (approvalLevelStatus.getUpdatedBy() != null) {
                        existingApprovalLevelStatus.setUpdatedBy(approvalLevelStatus.getUpdatedBy());
                    }
                    if (approvalLevelStatus.getUpdatedAt() != null) {
                        existingApprovalLevelStatus.setUpdatedAt(approvalLevelStatus.getUpdatedAt());
                    }

                    return existingApprovalLevelStatus;
                }
            )
            .map(approvalLevelStatusRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApprovalLevelStatus> findAll(Pageable pageable) {
        log.debug("Request to get all ApprovalLevelStatuses");
        return approvalLevelStatusRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ApprovalLevelStatus> findOne(Long id) {
        log.debug("Request to get ApprovalLevelStatus : {}", id);
        return approvalLevelStatusRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ApprovalLevelStatus : {}", id);
        approvalLevelStatusRepository.deleteById(id);
    }
}

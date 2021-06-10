package com.simplify.approval.service.impl;

import com.simplify.approval.domain.ApprovalRequest;
import com.simplify.approval.repository.ApprovalRequestRepository;
import com.simplify.approval.service.ApprovalRequestService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ApprovalRequest}.
 */
@Service
@Transactional
public class ApprovalRequestServiceImpl implements ApprovalRequestService {

    private final Logger log = LoggerFactory.getLogger(ApprovalRequestServiceImpl.class);

    private final ApprovalRequestRepository approvalRequestRepository;

    public ApprovalRequestServiceImpl(ApprovalRequestRepository approvalRequestRepository) {
        this.approvalRequestRepository = approvalRequestRepository;
    }

    @Override
    public ApprovalRequest save(ApprovalRequest approvalRequest) {
        log.debug("Request to save ApprovalRequest : {}", approvalRequest);
        return approvalRequestRepository.save(approvalRequest);
    }

    @Override
    public Optional<ApprovalRequest> partialUpdate(ApprovalRequest approvalRequest) {
        log.debug("Request to partially update ApprovalRequest : {}", approvalRequest);

        return approvalRequestRepository
            .findById(approvalRequest.getId())
            .map(
                existingApprovalRequest -> {
                    if (approvalRequest.getProgramId() != null) {
                        existingApprovalRequest.setProgramId(approvalRequest.getProgramId());
                    }
                    if (approvalRequest.getType() != null) {
                        existingApprovalRequest.setType(approvalRequest.getType());
                    }
                    if (approvalRequest.getApproveCallBackUrl() != null) {
                        existingApprovalRequest.setApproveCallBackUrl(approvalRequest.getApproveCallBackUrl());
                    }
                    if (approvalRequest.getRejectCallBackUrl() != null) {
                        existingApprovalRequest.setRejectCallBackUrl(approvalRequest.getRejectCallBackUrl());
                    }
                    if (approvalRequest.getCreatedBy() != null) {
                        existingApprovalRequest.setCreatedBy(approvalRequest.getCreatedBy());
                    }
                    if (approvalRequest.getCreatedAt() != null) {
                        existingApprovalRequest.setCreatedAt(approvalRequest.getCreatedAt());
                    }
                    if (approvalRequest.getUpdatedBy() != null) {
                        existingApprovalRequest.setUpdatedBy(approvalRequest.getUpdatedBy());
                    }
                    if (approvalRequest.getUpdatedAt() != null) {
                        existingApprovalRequest.setUpdatedAt(approvalRequest.getUpdatedAt());
                    }
                    if (approvalRequest.getFinalStatus() != null) {
                        existingApprovalRequest.setFinalStatus(approvalRequest.getFinalStatus());
                    }

                    return existingApprovalRequest;
                }
            )
            .map(approvalRequestRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApprovalRequest> findAll(Pageable pageable) {
        log.debug("Request to get all ApprovalRequests");
        return approvalRequestRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ApprovalRequest> findOne(Long id) {
        log.debug("Request to get ApprovalRequest : {}", id);
        return approvalRequestRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ApprovalRequest : {}", id);
        approvalRequestRepository.deleteById(id);
    }
}

package com.simplify.approval.service.impl;

import com.simplify.approval.domain.ApprovalRequestItem;
import com.simplify.approval.repository.ApprovalRequestItemRepository;
import com.simplify.approval.service.ApprovalRequestItemService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ApprovalRequestItem}.
 */
@Service
@Transactional
public class ApprovalRequestItemServiceImpl implements ApprovalRequestItemService {

    private final Logger log = LoggerFactory.getLogger(ApprovalRequestItemServiceImpl.class);

    private final ApprovalRequestItemRepository approvalRequestItemRepository;

    public ApprovalRequestItemServiceImpl(ApprovalRequestItemRepository approvalRequestItemRepository) {
        this.approvalRequestItemRepository = approvalRequestItemRepository;
    }

    @Override
    public ApprovalRequestItem save(ApprovalRequestItem approvalRequestItem) {
        log.debug("Request to save ApprovalRequestItem : {}", approvalRequestItem);
        return approvalRequestItemRepository.save(approvalRequestItem);
    }

    @Override
    public Optional<ApprovalRequestItem> partialUpdate(ApprovalRequestItem approvalRequestItem) {
        log.debug("Request to partially update ApprovalRequestItem : {}", approvalRequestItem);

        return approvalRequestItemRepository
            .findById(approvalRequestItem.getId())
            .map(
                existingApprovalRequestItem -> {
                    if (approvalRequestItem.getFieldName() != null) {
                        existingApprovalRequestItem.setFieldName(approvalRequestItem.getFieldName());
                    }
                    if (approvalRequestItem.getDataType() != null) {
                        existingApprovalRequestItem.setDataType(approvalRequestItem.getDataType());
                    }
                    if (approvalRequestItem.getStrValue() != null) {
                        existingApprovalRequestItem.setStrValue(approvalRequestItem.getStrValue());
                    }
                    if (approvalRequestItem.getDecValue() != null) {
                        existingApprovalRequestItem.setDecValue(approvalRequestItem.getDecValue());
                    }

                    return existingApprovalRequestItem;
                }
            )
            .map(approvalRequestItemRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApprovalRequestItem> findAll(Pageable pageable) {
        log.debug("Request to get all ApprovalRequestItems");
        return approvalRequestItemRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ApprovalRequestItem> findOne(Long id) {
        log.debug("Request to get ApprovalRequestItem : {}", id);
        return approvalRequestItemRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ApprovalRequestItem : {}", id);
        approvalRequestItemRepository.deleteById(id);
    }
}

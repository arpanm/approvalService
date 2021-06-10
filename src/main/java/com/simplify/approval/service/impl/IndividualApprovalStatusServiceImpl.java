package com.simplify.approval.service.impl;

import com.simplify.approval.domain.IndividualApprovalStatus;
import com.simplify.approval.repository.IndividualApprovalStatusRepository;
import com.simplify.approval.service.IndividualApprovalStatusService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IndividualApprovalStatus}.
 */
@Service
@Transactional
public class IndividualApprovalStatusServiceImpl implements IndividualApprovalStatusService {

    private final Logger log = LoggerFactory.getLogger(IndividualApprovalStatusServiceImpl.class);

    private final IndividualApprovalStatusRepository individualApprovalStatusRepository;

    public IndividualApprovalStatusServiceImpl(IndividualApprovalStatusRepository individualApprovalStatusRepository) {
        this.individualApprovalStatusRepository = individualApprovalStatusRepository;
    }

    @Override
    public IndividualApprovalStatus save(IndividualApprovalStatus individualApprovalStatus) {
        log.debug("Request to save IndividualApprovalStatus : {}", individualApprovalStatus);
        return individualApprovalStatusRepository.save(individualApprovalStatus);
    }

    @Override
    public Optional<IndividualApprovalStatus> partialUpdate(IndividualApprovalStatus individualApprovalStatus) {
        log.debug("Request to partially update IndividualApprovalStatus : {}", individualApprovalStatus);

        return individualApprovalStatusRepository
            .findById(individualApprovalStatus.getId())
            .map(
                existingIndividualApprovalStatus -> {
                    if (individualApprovalStatus.getStatus() != null) {
                        existingIndividualApprovalStatus.setStatus(individualApprovalStatus.getStatus());
                    }
                    if (individualApprovalStatus.getClientTime() != null) {
                        existingIndividualApprovalStatus.setClientTime(individualApprovalStatus.getClientTime());
                    }
                    if (individualApprovalStatus.getCreatedBy() != null) {
                        existingIndividualApprovalStatus.setCreatedBy(individualApprovalStatus.getCreatedBy());
                    }
                    if (individualApprovalStatus.getCreatedAt() != null) {
                        existingIndividualApprovalStatus.setCreatedAt(individualApprovalStatus.getCreatedAt());
                    }
                    if (individualApprovalStatus.getUpdatedBy() != null) {
                        existingIndividualApprovalStatus.setUpdatedBy(individualApprovalStatus.getUpdatedBy());
                    }
                    if (individualApprovalStatus.getUpdatedAt() != null) {
                        existingIndividualApprovalStatus.setUpdatedAt(individualApprovalStatus.getUpdatedAt());
                    }

                    return existingIndividualApprovalStatus;
                }
            )
            .map(individualApprovalStatusRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IndividualApprovalStatus> findAll(Pageable pageable) {
        log.debug("Request to get all IndividualApprovalStatuses");
        return individualApprovalStatusRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IndividualApprovalStatus> findOne(Long id) {
        log.debug("Request to get IndividualApprovalStatus : {}", id);
        return individualApprovalStatusRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete IndividualApprovalStatus : {}", id);
        individualApprovalStatusRepository.deleteById(id);
    }
}

package com.simplify.approval.service.impl;

import com.simplify.approval.domain.Approver;
import com.simplify.approval.repository.ApproverRepository;
import com.simplify.approval.service.ApproverService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Approver}.
 */
@Service
@Transactional
public class ApproverServiceImpl implements ApproverService {

    private final Logger log = LoggerFactory.getLogger(ApproverServiceImpl.class);

    private final ApproverRepository approverRepository;

    public ApproverServiceImpl(ApproverRepository approverRepository) {
        this.approverRepository = approverRepository;
    }

    @Override
    public Approver save(Approver approver) {
        log.debug("Request to save Approver : {}", approver);
        return approverRepository.save(approver);
    }

    @Override
    public Optional<Approver> partialUpdate(Approver approver) {
        log.debug("Request to partially update Approver : {}", approver);

        return approverRepository
            .findById(approver.getId())
            .map(
                existingApprover -> {
                    if (approver.getProgramUserId() != null) {
                        existingApprover.setProgramUserId(approver.getProgramUserId());
                    }
                    if (approver.getEmail() != null) {
                        existingApprover.setEmail(approver.getEmail());
                    }
                    if (approver.getLevel() != null) {
                        existingApprover.setLevel(approver.getLevel());
                    }

                    return existingApprover;
                }
            )
            .map(approverRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Approver> findAll(Pageable pageable) {
        log.debug("Request to get all Approvers");
        return approverRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Approver> findOne(Long id) {
        log.debug("Request to get Approver : {}", id);
        return approverRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Approver : {}", id);
        approverRepository.deleteById(id);
    }
}

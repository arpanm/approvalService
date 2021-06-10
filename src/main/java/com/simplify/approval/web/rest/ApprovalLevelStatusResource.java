package com.simplify.approval.web.rest;

import com.simplify.approval.domain.ApprovalLevelStatus;
import com.simplify.approval.repository.ApprovalLevelStatusRepository;
import com.simplify.approval.service.ApprovalLevelStatusService;
import com.simplify.approval.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.simplify.approval.domain.ApprovalLevelStatus}.
 */
@RestController
@RequestMapping("/api")
public class ApprovalLevelStatusResource {

    private final Logger log = LoggerFactory.getLogger(ApprovalLevelStatusResource.class);

    private static final String ENTITY_NAME = "approvalLevelStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApprovalLevelStatusService approvalLevelStatusService;

    private final ApprovalLevelStatusRepository approvalLevelStatusRepository;

    public ApprovalLevelStatusResource(
        ApprovalLevelStatusService approvalLevelStatusService,
        ApprovalLevelStatusRepository approvalLevelStatusRepository
    ) {
        this.approvalLevelStatusService = approvalLevelStatusService;
        this.approvalLevelStatusRepository = approvalLevelStatusRepository;
    }

    /**
     * {@code POST  /approval-level-statuses} : Create a new approvalLevelStatus.
     *
     * @param approvalLevelStatus the approvalLevelStatus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new approvalLevelStatus, or with status {@code 400 (Bad Request)} if the approvalLevelStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/approval-level-statuses")
    public ResponseEntity<ApprovalLevelStatus> createApprovalLevelStatus(@Valid @RequestBody ApprovalLevelStatus approvalLevelStatus)
        throws URISyntaxException {
        log.debug("REST request to save ApprovalLevelStatus : {}", approvalLevelStatus);
        if (approvalLevelStatus.getId() != null) {
            throw new BadRequestAlertException("A new approvalLevelStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApprovalLevelStatus result = approvalLevelStatusService.save(approvalLevelStatus);
        return ResponseEntity
            .created(new URI("/api/approval-level-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /approval-level-statuses/:id} : Updates an existing approvalLevelStatus.
     *
     * @param id the id of the approvalLevelStatus to save.
     * @param approvalLevelStatus the approvalLevelStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated approvalLevelStatus,
     * or with status {@code 400 (Bad Request)} if the approvalLevelStatus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the approvalLevelStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/approval-level-statuses/{id}")
    public ResponseEntity<ApprovalLevelStatus> updateApprovalLevelStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ApprovalLevelStatus approvalLevelStatus
    ) throws URISyntaxException {
        log.debug("REST request to update ApprovalLevelStatus : {}, {}", id, approvalLevelStatus);
        if (approvalLevelStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, approvalLevelStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!approvalLevelStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ApprovalLevelStatus result = approvalLevelStatusService.save(approvalLevelStatus);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, approvalLevelStatus.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /approval-level-statuses/:id} : Partial updates given fields of an existing approvalLevelStatus, field will ignore if it is null
     *
     * @param id the id of the approvalLevelStatus to save.
     * @param approvalLevelStatus the approvalLevelStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated approvalLevelStatus,
     * or with status {@code 400 (Bad Request)} if the approvalLevelStatus is not valid,
     * or with status {@code 404 (Not Found)} if the approvalLevelStatus is not found,
     * or with status {@code 500 (Internal Server Error)} if the approvalLevelStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/approval-level-statuses/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ApprovalLevelStatus> partialUpdateApprovalLevelStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ApprovalLevelStatus approvalLevelStatus
    ) throws URISyntaxException {
        log.debug("REST request to partial update ApprovalLevelStatus partially : {}, {}", id, approvalLevelStatus);
        if (approvalLevelStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, approvalLevelStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!approvalLevelStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApprovalLevelStatus> result = approvalLevelStatusService.partialUpdate(approvalLevelStatus);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, approvalLevelStatus.getId().toString())
        );
    }

    /**
     * {@code GET  /approval-level-statuses} : get all the approvalLevelStatuses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of approvalLevelStatuses in body.
     */
    @GetMapping("/approval-level-statuses")
    public ResponseEntity<List<ApprovalLevelStatus>> getAllApprovalLevelStatuses(Pageable pageable) {
        log.debug("REST request to get a page of ApprovalLevelStatuses");
        Page<ApprovalLevelStatus> page = approvalLevelStatusService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /approval-level-statuses/:id} : get the "id" approvalLevelStatus.
     *
     * @param id the id of the approvalLevelStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the approvalLevelStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/approval-level-statuses/{id}")
    public ResponseEntity<ApprovalLevelStatus> getApprovalLevelStatus(@PathVariable Long id) {
        log.debug("REST request to get ApprovalLevelStatus : {}", id);
        Optional<ApprovalLevelStatus> approvalLevelStatus = approvalLevelStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(approvalLevelStatus);
    }

    /**
     * {@code DELETE  /approval-level-statuses/:id} : delete the "id" approvalLevelStatus.
     *
     * @param id the id of the approvalLevelStatus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/approval-level-statuses/{id}")
    public ResponseEntity<Void> deleteApprovalLevelStatus(@PathVariable Long id) {
        log.debug("REST request to delete ApprovalLevelStatus : {}", id);
        approvalLevelStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

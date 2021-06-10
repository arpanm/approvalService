package com.simplify.approval.web.rest;

import com.simplify.approval.domain.ApprovalRequest;
import com.simplify.approval.repository.ApprovalRequestRepository;
import com.simplify.approval.service.ApprovalRequestService;
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
 * REST controller for managing {@link com.simplify.approval.domain.ApprovalRequest}.
 */
@RestController
@RequestMapping("/api")
public class ApprovalRequestResource {

    private final Logger log = LoggerFactory.getLogger(ApprovalRequestResource.class);

    private static final String ENTITY_NAME = "approvalRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApprovalRequestService approvalRequestService;

    private final ApprovalRequestRepository approvalRequestRepository;

    public ApprovalRequestResource(ApprovalRequestService approvalRequestService, ApprovalRequestRepository approvalRequestRepository) {
        this.approvalRequestService = approvalRequestService;
        this.approvalRequestRepository = approvalRequestRepository;
    }

    /**
     * {@code POST  /approval-requests} : Create a new approvalRequest.
     *
     * @param approvalRequest the approvalRequest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new approvalRequest, or with status {@code 400 (Bad Request)} if the approvalRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/approval-requests")
    public ResponseEntity<ApprovalRequest> createApprovalRequest(@Valid @RequestBody ApprovalRequest approvalRequest)
        throws URISyntaxException {
        log.debug("REST request to save ApprovalRequest : {}", approvalRequest);
        if (approvalRequest.getId() != null) {
            throw new BadRequestAlertException("A new approvalRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApprovalRequest result = approvalRequestService.save(approvalRequest);
        return ResponseEntity
            .created(new URI("/api/approval-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /approval-requests/:id} : Updates an existing approvalRequest.
     *
     * @param id the id of the approvalRequest to save.
     * @param approvalRequest the approvalRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated approvalRequest,
     * or with status {@code 400 (Bad Request)} if the approvalRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the approvalRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/approval-requests/{id}")
    public ResponseEntity<ApprovalRequest> updateApprovalRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ApprovalRequest approvalRequest
    ) throws URISyntaxException {
        log.debug("REST request to update ApprovalRequest : {}, {}", id, approvalRequest);
        if (approvalRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, approvalRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!approvalRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ApprovalRequest result = approvalRequestService.save(approvalRequest);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, approvalRequest.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /approval-requests/:id} : Partial updates given fields of an existing approvalRequest, field will ignore if it is null
     *
     * @param id the id of the approvalRequest to save.
     * @param approvalRequest the approvalRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated approvalRequest,
     * or with status {@code 400 (Bad Request)} if the approvalRequest is not valid,
     * or with status {@code 404 (Not Found)} if the approvalRequest is not found,
     * or with status {@code 500 (Internal Server Error)} if the approvalRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/approval-requests/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ApprovalRequest> partialUpdateApprovalRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ApprovalRequest approvalRequest
    ) throws URISyntaxException {
        log.debug("REST request to partial update ApprovalRequest partially : {}, {}", id, approvalRequest);
        if (approvalRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, approvalRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!approvalRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApprovalRequest> result = approvalRequestService.partialUpdate(approvalRequest);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, approvalRequest.getId().toString())
        );
    }

    /**
     * {@code GET  /approval-requests} : get all the approvalRequests.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of approvalRequests in body.
     */
    @GetMapping("/approval-requests")
    public ResponseEntity<List<ApprovalRequest>> getAllApprovalRequests(Pageable pageable) {
        log.debug("REST request to get a page of ApprovalRequests");
        Page<ApprovalRequest> page = approvalRequestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /approval-requests/:id} : get the "id" approvalRequest.
     *
     * @param id the id of the approvalRequest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the approvalRequest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/approval-requests/{id}")
    public ResponseEntity<ApprovalRequest> getApprovalRequest(@PathVariable Long id) {
        log.debug("REST request to get ApprovalRequest : {}", id);
        Optional<ApprovalRequest> approvalRequest = approvalRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(approvalRequest);
    }

    /**
     * {@code DELETE  /approval-requests/:id} : delete the "id" approvalRequest.
     *
     * @param id the id of the approvalRequest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/approval-requests/{id}")
    public ResponseEntity<Void> deleteApprovalRequest(@PathVariable Long id) {
        log.debug("REST request to delete ApprovalRequest : {}", id);
        approvalRequestService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

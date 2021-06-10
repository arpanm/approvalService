package com.simplify.approval.web.rest;

import com.simplify.approval.domain.IndividualApprovalStatus;
import com.simplify.approval.repository.IndividualApprovalStatusRepository;
import com.simplify.approval.service.IndividualApprovalStatusService;
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
 * REST controller for managing {@link com.simplify.approval.domain.IndividualApprovalStatus}.
 */
@RestController
@RequestMapping("/api")
public class IndividualApprovalStatusResource {

    private final Logger log = LoggerFactory.getLogger(IndividualApprovalStatusResource.class);

    private static final String ENTITY_NAME = "individualApprovalStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IndividualApprovalStatusService individualApprovalStatusService;

    private final IndividualApprovalStatusRepository individualApprovalStatusRepository;

    public IndividualApprovalStatusResource(
        IndividualApprovalStatusService individualApprovalStatusService,
        IndividualApprovalStatusRepository individualApprovalStatusRepository
    ) {
        this.individualApprovalStatusService = individualApprovalStatusService;
        this.individualApprovalStatusRepository = individualApprovalStatusRepository;
    }

    /**
     * {@code POST  /individual-approval-statuses} : Create a new individualApprovalStatus.
     *
     * @param individualApprovalStatus the individualApprovalStatus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new individualApprovalStatus, or with status {@code 400 (Bad Request)} if the individualApprovalStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/individual-approval-statuses")
    public ResponseEntity<IndividualApprovalStatus> createIndividualApprovalStatus(
        @Valid @RequestBody IndividualApprovalStatus individualApprovalStatus
    ) throws URISyntaxException {
        log.debug("REST request to save IndividualApprovalStatus : {}", individualApprovalStatus);
        if (individualApprovalStatus.getId() != null) {
            throw new BadRequestAlertException("A new individualApprovalStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IndividualApprovalStatus result = individualApprovalStatusService.save(individualApprovalStatus);
        return ResponseEntity
            .created(new URI("/api/individual-approval-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /individual-approval-statuses/:id} : Updates an existing individualApprovalStatus.
     *
     * @param id the id of the individualApprovalStatus to save.
     * @param individualApprovalStatus the individualApprovalStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated individualApprovalStatus,
     * or with status {@code 400 (Bad Request)} if the individualApprovalStatus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the individualApprovalStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/individual-approval-statuses/{id}")
    public ResponseEntity<IndividualApprovalStatus> updateIndividualApprovalStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IndividualApprovalStatus individualApprovalStatus
    ) throws URISyntaxException {
        log.debug("REST request to update IndividualApprovalStatus : {}, {}", id, individualApprovalStatus);
        if (individualApprovalStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, individualApprovalStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!individualApprovalStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IndividualApprovalStatus result = individualApprovalStatusService.save(individualApprovalStatus);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, individualApprovalStatus.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /individual-approval-statuses/:id} : Partial updates given fields of an existing individualApprovalStatus, field will ignore if it is null
     *
     * @param id the id of the individualApprovalStatus to save.
     * @param individualApprovalStatus the individualApprovalStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated individualApprovalStatus,
     * or with status {@code 400 (Bad Request)} if the individualApprovalStatus is not valid,
     * or with status {@code 404 (Not Found)} if the individualApprovalStatus is not found,
     * or with status {@code 500 (Internal Server Error)} if the individualApprovalStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/individual-approval-statuses/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<IndividualApprovalStatus> partialUpdateIndividualApprovalStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IndividualApprovalStatus individualApprovalStatus
    ) throws URISyntaxException {
        log.debug("REST request to partial update IndividualApprovalStatus partially : {}, {}", id, individualApprovalStatus);
        if (individualApprovalStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, individualApprovalStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!individualApprovalStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IndividualApprovalStatus> result = individualApprovalStatusService.partialUpdate(individualApprovalStatus);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, individualApprovalStatus.getId().toString())
        );
    }

    /**
     * {@code GET  /individual-approval-statuses} : get all the individualApprovalStatuses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of individualApprovalStatuses in body.
     */
    @GetMapping("/individual-approval-statuses")
    public ResponseEntity<List<IndividualApprovalStatus>> getAllIndividualApprovalStatuses(Pageable pageable) {
        log.debug("REST request to get a page of IndividualApprovalStatuses");
        Page<IndividualApprovalStatus> page = individualApprovalStatusService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /individual-approval-statuses/:id} : get the "id" individualApprovalStatus.
     *
     * @param id the id of the individualApprovalStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the individualApprovalStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/individual-approval-statuses/{id}")
    public ResponseEntity<IndividualApprovalStatus> getIndividualApprovalStatus(@PathVariable Long id) {
        log.debug("REST request to get IndividualApprovalStatus : {}", id);
        Optional<IndividualApprovalStatus> individualApprovalStatus = individualApprovalStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(individualApprovalStatus);
    }

    /**
     * {@code DELETE  /individual-approval-statuses/:id} : delete the "id" individualApprovalStatus.
     *
     * @param id the id of the individualApprovalStatus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/individual-approval-statuses/{id}")
    public ResponseEntity<Void> deleteIndividualApprovalStatus(@PathVariable Long id) {
        log.debug("REST request to delete IndividualApprovalStatus : {}", id);
        individualApprovalStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

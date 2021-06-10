package com.simplify.approval.web.rest;

import com.simplify.approval.domain.ApprovalRequestItem;
import com.simplify.approval.repository.ApprovalRequestItemRepository;
import com.simplify.approval.service.ApprovalRequestItemService;
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
 * REST controller for managing {@link com.simplify.approval.domain.ApprovalRequestItem}.
 */
@RestController
@RequestMapping("/api")
public class ApprovalRequestItemResource {

    private final Logger log = LoggerFactory.getLogger(ApprovalRequestItemResource.class);

    private static final String ENTITY_NAME = "approvalRequestItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApprovalRequestItemService approvalRequestItemService;

    private final ApprovalRequestItemRepository approvalRequestItemRepository;

    public ApprovalRequestItemResource(
        ApprovalRequestItemService approvalRequestItemService,
        ApprovalRequestItemRepository approvalRequestItemRepository
    ) {
        this.approvalRequestItemService = approvalRequestItemService;
        this.approvalRequestItemRepository = approvalRequestItemRepository;
    }

    /**
     * {@code POST  /approval-request-items} : Create a new approvalRequestItem.
     *
     * @param approvalRequestItem the approvalRequestItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new approvalRequestItem, or with status {@code 400 (Bad Request)} if the approvalRequestItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/approval-request-items")
    public ResponseEntity<ApprovalRequestItem> createApprovalRequestItem(@Valid @RequestBody ApprovalRequestItem approvalRequestItem)
        throws URISyntaxException {
        log.debug("REST request to save ApprovalRequestItem : {}", approvalRequestItem);
        if (approvalRequestItem.getId() != null) {
            throw new BadRequestAlertException("A new approvalRequestItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApprovalRequestItem result = approvalRequestItemService.save(approvalRequestItem);
        return ResponseEntity
            .created(new URI("/api/approval-request-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /approval-request-items/:id} : Updates an existing approvalRequestItem.
     *
     * @param id the id of the approvalRequestItem to save.
     * @param approvalRequestItem the approvalRequestItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated approvalRequestItem,
     * or with status {@code 400 (Bad Request)} if the approvalRequestItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the approvalRequestItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/approval-request-items/{id}")
    public ResponseEntity<ApprovalRequestItem> updateApprovalRequestItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ApprovalRequestItem approvalRequestItem
    ) throws URISyntaxException {
        log.debug("REST request to update ApprovalRequestItem : {}, {}", id, approvalRequestItem);
        if (approvalRequestItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, approvalRequestItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!approvalRequestItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ApprovalRequestItem result = approvalRequestItemService.save(approvalRequestItem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, approvalRequestItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /approval-request-items/:id} : Partial updates given fields of an existing approvalRequestItem, field will ignore if it is null
     *
     * @param id the id of the approvalRequestItem to save.
     * @param approvalRequestItem the approvalRequestItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated approvalRequestItem,
     * or with status {@code 400 (Bad Request)} if the approvalRequestItem is not valid,
     * or with status {@code 404 (Not Found)} if the approvalRequestItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the approvalRequestItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/approval-request-items/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ApprovalRequestItem> partialUpdateApprovalRequestItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ApprovalRequestItem approvalRequestItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update ApprovalRequestItem partially : {}, {}", id, approvalRequestItem);
        if (approvalRequestItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, approvalRequestItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!approvalRequestItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApprovalRequestItem> result = approvalRequestItemService.partialUpdate(approvalRequestItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, approvalRequestItem.getId().toString())
        );
    }

    /**
     * {@code GET  /approval-request-items} : get all the approvalRequestItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of approvalRequestItems in body.
     */
    @GetMapping("/approval-request-items")
    public ResponseEntity<List<ApprovalRequestItem>> getAllApprovalRequestItems(Pageable pageable) {
        log.debug("REST request to get a page of ApprovalRequestItems");
        Page<ApprovalRequestItem> page = approvalRequestItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /approval-request-items/:id} : get the "id" approvalRequestItem.
     *
     * @param id the id of the approvalRequestItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the approvalRequestItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/approval-request-items/{id}")
    public ResponseEntity<ApprovalRequestItem> getApprovalRequestItem(@PathVariable Long id) {
        log.debug("REST request to get ApprovalRequestItem : {}", id);
        Optional<ApprovalRequestItem> approvalRequestItem = approvalRequestItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(approvalRequestItem);
    }

    /**
     * {@code DELETE  /approval-request-items/:id} : delete the "id" approvalRequestItem.
     *
     * @param id the id of the approvalRequestItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/approval-request-items/{id}")
    public ResponseEntity<Void> deleteApprovalRequestItem(@PathVariable Long id) {
        log.debug("REST request to delete ApprovalRequestItem : {}", id);
        approvalRequestItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

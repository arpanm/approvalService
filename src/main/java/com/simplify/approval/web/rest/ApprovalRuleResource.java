package com.simplify.approval.web.rest;

import com.simplify.approval.domain.ApprovalRule;
import com.simplify.approval.repository.ApprovalRuleRepository;
import com.simplify.approval.service.ApprovalRuleService;
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
 * REST controller for managing {@link com.simplify.approval.domain.ApprovalRule}.
 */
@RestController
@RequestMapping("/api")
public class ApprovalRuleResource {

    private final Logger log = LoggerFactory.getLogger(ApprovalRuleResource.class);

    private static final String ENTITY_NAME = "approvalRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApprovalRuleService approvalRuleService;

    private final ApprovalRuleRepository approvalRuleRepository;

    public ApprovalRuleResource(ApprovalRuleService approvalRuleService, ApprovalRuleRepository approvalRuleRepository) {
        this.approvalRuleService = approvalRuleService;
        this.approvalRuleRepository = approvalRuleRepository;
    }

    /**
     * {@code POST  /approval-rules} : Create a new approvalRule.
     *
     * @param approvalRule the approvalRule to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new approvalRule, or with status {@code 400 (Bad Request)} if the approvalRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/approval-rules")
    public ResponseEntity<ApprovalRule> createApprovalRule(@Valid @RequestBody ApprovalRule approvalRule) throws URISyntaxException {
        log.debug("REST request to save ApprovalRule : {}", approvalRule);
        if (approvalRule.getId() != null) {
            throw new BadRequestAlertException("A new approvalRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApprovalRule result = approvalRuleService.save(approvalRule);
        return ResponseEntity
            .created(new URI("/api/approval-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /approval-rules/:id} : Updates an existing approvalRule.
     *
     * @param id the id of the approvalRule to save.
     * @param approvalRule the approvalRule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated approvalRule,
     * or with status {@code 400 (Bad Request)} if the approvalRule is not valid,
     * or with status {@code 500 (Internal Server Error)} if the approvalRule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/approval-rules/{id}")
    public ResponseEntity<ApprovalRule> updateApprovalRule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ApprovalRule approvalRule
    ) throws URISyntaxException {
        log.debug("REST request to update ApprovalRule : {}, {}", id, approvalRule);
        if (approvalRule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, approvalRule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!approvalRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ApprovalRule result = approvalRuleService.save(approvalRule);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, approvalRule.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /approval-rules/:id} : Partial updates given fields of an existing approvalRule, field will ignore if it is null
     *
     * @param id the id of the approvalRule to save.
     * @param approvalRule the approvalRule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated approvalRule,
     * or with status {@code 400 (Bad Request)} if the approvalRule is not valid,
     * or with status {@code 404 (Not Found)} if the approvalRule is not found,
     * or with status {@code 500 (Internal Server Error)} if the approvalRule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/approval-rules/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ApprovalRule> partialUpdateApprovalRule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ApprovalRule approvalRule
    ) throws URISyntaxException {
        log.debug("REST request to partial update ApprovalRule partially : {}, {}", id, approvalRule);
        if (approvalRule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, approvalRule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!approvalRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApprovalRule> result = approvalRuleService.partialUpdate(approvalRule);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, approvalRule.getId().toString())
        );
    }

    /**
     * {@code GET  /approval-rules} : get all the approvalRules.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of approvalRules in body.
     */
    @GetMapping("/approval-rules")
    public ResponseEntity<List<ApprovalRule>> getAllApprovalRules(Pageable pageable) {
        log.debug("REST request to get a page of ApprovalRules");
        Page<ApprovalRule> page = approvalRuleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /approval-rules/:id} : get the "id" approvalRule.
     *
     * @param id the id of the approvalRule to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the approvalRule, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/approval-rules/{id}")
    public ResponseEntity<ApprovalRule> getApprovalRule(@PathVariable Long id) {
        log.debug("REST request to get ApprovalRule : {}", id);
        Optional<ApprovalRule> approvalRule = approvalRuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(approvalRule);
    }

    /**
     * {@code DELETE  /approval-rules/:id} : delete the "id" approvalRule.
     *
     * @param id the id of the approvalRule to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/approval-rules/{id}")
    public ResponseEntity<Void> deleteApprovalRule(@PathVariable Long id) {
        log.debug("REST request to delete ApprovalRule : {}", id);
        approvalRuleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

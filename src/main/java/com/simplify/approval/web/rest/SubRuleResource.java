package com.simplify.approval.web.rest;

import com.simplify.approval.domain.SubRule;
import com.simplify.approval.repository.SubRuleRepository;
import com.simplify.approval.service.SubRuleService;
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
 * REST controller for managing {@link com.simplify.approval.domain.SubRule}.
 */
@RestController
@RequestMapping("/api")
public class SubRuleResource {

    private final Logger log = LoggerFactory.getLogger(SubRuleResource.class);

    private static final String ENTITY_NAME = "subRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubRuleService subRuleService;

    private final SubRuleRepository subRuleRepository;

    public SubRuleResource(SubRuleService subRuleService, SubRuleRepository subRuleRepository) {
        this.subRuleService = subRuleService;
        this.subRuleRepository = subRuleRepository;
    }

    /**
     * {@code POST  /sub-rules} : Create a new subRule.
     *
     * @param subRule the subRule to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subRule, or with status {@code 400 (Bad Request)} if the subRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sub-rules")
    public ResponseEntity<SubRule> createSubRule(@Valid @RequestBody SubRule subRule) throws URISyntaxException {
        log.debug("REST request to save SubRule : {}", subRule);
        if (subRule.getId() != null) {
            throw new BadRequestAlertException("A new subRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubRule result = subRuleService.save(subRule);
        return ResponseEntity
            .created(new URI("/api/sub-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sub-rules/:id} : Updates an existing subRule.
     *
     * @param id the id of the subRule to save.
     * @param subRule the subRule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subRule,
     * or with status {@code 400 (Bad Request)} if the subRule is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subRule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sub-rules/{id}")
    public ResponseEntity<SubRule> updateSubRule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubRule subRule
    ) throws URISyntaxException {
        log.debug("REST request to update SubRule : {}, {}", id, subRule);
        if (subRule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subRule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SubRule result = subRuleService.save(subRule);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subRule.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sub-rules/:id} : Partial updates given fields of an existing subRule, field will ignore if it is null
     *
     * @param id the id of the subRule to save.
     * @param subRule the subRule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subRule,
     * or with status {@code 400 (Bad Request)} if the subRule is not valid,
     * or with status {@code 404 (Not Found)} if the subRule is not found,
     * or with status {@code 500 (Internal Server Error)} if the subRule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sub-rules/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SubRule> partialUpdateSubRule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubRule subRule
    ) throws URISyntaxException {
        log.debug("REST request to partial update SubRule partially : {}, {}", id, subRule);
        if (subRule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subRule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubRule> result = subRuleService.partialUpdate(subRule);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subRule.getId().toString())
        );
    }

    /**
     * {@code GET  /sub-rules} : get all the subRules.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subRules in body.
     */
    @GetMapping("/sub-rules")
    public ResponseEntity<List<SubRule>> getAllSubRules(Pageable pageable) {
        log.debug("REST request to get a page of SubRules");
        Page<SubRule> page = subRuleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sub-rules/:id} : get the "id" subRule.
     *
     * @param id the id of the subRule to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subRule, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sub-rules/{id}")
    public ResponseEntity<SubRule> getSubRule(@PathVariable Long id) {
        log.debug("REST request to get SubRule : {}", id);
        Optional<SubRule> subRule = subRuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subRule);
    }

    /**
     * {@code DELETE  /sub-rules/:id} : delete the "id" subRule.
     *
     * @param id the id of the subRule to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sub-rules/{id}")
    public ResponseEntity<Void> deleteSubRule(@PathVariable Long id) {
        log.debug("REST request to delete SubRule : {}", id);
        subRuleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

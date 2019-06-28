package com.spingular.proposal.web.rest;

import com.spingular.proposal.service.ProposalUserService;
import com.spingular.proposal.web.rest.errors.BadRequestAlertException;
import com.spingular.proposal.service.dto.ProposalUserDTO;
import com.spingular.proposal.service.dto.ProposalUserCriteria;
import com.spingular.proposal.service.ProposalUserQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.spingular.proposal.domain.ProposalUser}.
 */
@RestController
@RequestMapping("/api")
public class ProposalUserResource {

    private final Logger log = LoggerFactory.getLogger(ProposalUserResource.class);

    private static final String ENTITY_NAME = "proposalUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProposalUserService proposalUserService;

    private final ProposalUserQueryService proposalUserQueryService;

    public ProposalUserResource(ProposalUserService proposalUserService, ProposalUserQueryService proposalUserQueryService) {
        this.proposalUserService = proposalUserService;
        this.proposalUserQueryService = proposalUserQueryService;
    }

    /**
     * {@code POST  /proposal-users} : Create a new proposalUser.
     *
     * @param proposalUserDTO the proposalUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new proposalUserDTO, or with status {@code 400 (Bad Request)} if the proposalUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/proposal-users")
    public ResponseEntity<ProposalUserDTO> createProposalUser(@Valid @RequestBody ProposalUserDTO proposalUserDTO) throws URISyntaxException {
        log.debug("REST request to save ProposalUser : {}", proposalUserDTO);
        if (proposalUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new proposalUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProposalUserDTO result = proposalUserService.save(proposalUserDTO);
        return ResponseEntity.created(new URI("/api/proposal-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /proposal-users} : Updates an existing proposalUser.
     *
     * @param proposalUserDTO the proposalUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proposalUserDTO,
     * or with status {@code 400 (Bad Request)} if the proposalUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the proposalUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/proposal-users")
    public ResponseEntity<ProposalUserDTO> updateProposalUser(@Valid @RequestBody ProposalUserDTO proposalUserDTO) throws URISyntaxException {
        log.debug("REST request to update ProposalUser : {}", proposalUserDTO);
        if (proposalUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProposalUserDTO result = proposalUserService.save(proposalUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, proposalUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /proposal-users} : get all the proposalUsers.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of proposalUsers in body.
     */
    @GetMapping("/proposal-users")
    public ResponseEntity<List<ProposalUserDTO>> getAllProposalUsers(ProposalUserCriteria criteria, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get ProposalUsers by criteria: {}", criteria);
        Page<ProposalUserDTO> page = proposalUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /proposal-users/count} : count all the proposalUsers.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/proposal-users/count")
    public ResponseEntity<Long> countProposalUsers(ProposalUserCriteria criteria) {
        log.debug("REST request to count ProposalUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(proposalUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /proposal-users/:id} : get the "id" proposalUser.
     *
     * @param id the id of the proposalUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the proposalUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/proposal-users/{id}")
    public ResponseEntity<ProposalUserDTO> getProposalUser(@PathVariable Long id) {
        log.debug("REST request to get ProposalUser : {}", id);
        Optional<ProposalUserDTO> proposalUserDTO = proposalUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(proposalUserDTO);
    }

    /**
     * {@code DELETE  /proposal-users/:id} : delete the "id" proposalUser.
     *
     * @param id the id of the proposalUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/proposal-users/{id}")
    public ResponseEntity<Void> deleteProposalUser(@PathVariable Long id) {
        log.debug("REST request to delete ProposalUser : {}", id);
        proposalUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

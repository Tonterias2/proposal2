package com.spingular.proposal.web.rest;

import com.spingular.proposal.service.VoteProposalService;
import com.spingular.proposal.web.rest.errors.BadRequestAlertException;
import com.spingular.proposal.service.dto.VoteProposalDTO;
import com.spingular.proposal.service.dto.VoteProposalCriteria;
import com.spingular.proposal.service.VoteProposalQueryService;

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
 * REST controller for managing {@link com.spingular.proposal.domain.VoteProposal}.
 */
@RestController
@RequestMapping("/api")
public class VoteProposalResource {

    private final Logger log = LoggerFactory.getLogger(VoteProposalResource.class);

    private static final String ENTITY_NAME = "voteProposal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VoteProposalService voteProposalService;

    private final VoteProposalQueryService voteProposalQueryService;

    public VoteProposalResource(VoteProposalService voteProposalService, VoteProposalQueryService voteProposalQueryService) {
        this.voteProposalService = voteProposalService;
        this.voteProposalQueryService = voteProposalQueryService;
    }

    /**
     * {@code POST  /vote-proposals} : Create a new voteProposal.
     *
     * @param voteProposalDTO the voteProposalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new voteProposalDTO, or with status {@code 400 (Bad Request)} if the voteProposal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vote-proposals")
    public ResponseEntity<VoteProposalDTO> createVoteProposal(@Valid @RequestBody VoteProposalDTO voteProposalDTO) throws URISyntaxException {
        log.debug("REST request to save VoteProposal : {}", voteProposalDTO);
        if (voteProposalDTO.getId() != null) {
            throw new BadRequestAlertException("A new voteProposal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VoteProposalDTO result = voteProposalService.save(voteProposalDTO);
        return ResponseEntity.created(new URI("/api/vote-proposals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vote-proposals} : Updates an existing voteProposal.
     *
     * @param voteProposalDTO the voteProposalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voteProposalDTO,
     * or with status {@code 400 (Bad Request)} if the voteProposalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the voteProposalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vote-proposals")
    public ResponseEntity<VoteProposalDTO> updateVoteProposal(@Valid @RequestBody VoteProposalDTO voteProposalDTO) throws URISyntaxException {
        log.debug("REST request to update VoteProposal : {}", voteProposalDTO);
        if (voteProposalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VoteProposalDTO result = voteProposalService.save(voteProposalDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, voteProposalDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /vote-proposals} : get all the voteProposals.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of voteProposals in body.
     */
    @GetMapping("/vote-proposals")
    public ResponseEntity<List<VoteProposalDTO>> getAllVoteProposals(VoteProposalCriteria criteria, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get VoteProposals by criteria: {}", criteria);
        Page<VoteProposalDTO> page = voteProposalQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /vote-proposals/count} : count all the voteProposals.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/vote-proposals/count")
    public ResponseEntity<Long> countVoteProposals(VoteProposalCriteria criteria) {
        log.debug("REST request to count VoteProposals by criteria: {}", criteria);
        return ResponseEntity.ok().body(voteProposalQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vote-proposals/:id} : get the "id" voteProposal.
     *
     * @param id the id of the voteProposalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the voteProposalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vote-proposals/{id}")
    public ResponseEntity<VoteProposalDTO> getVoteProposal(@PathVariable Long id) {
        log.debug("REST request to get VoteProposal : {}", id);
        Optional<VoteProposalDTO> voteProposalDTO = voteProposalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(voteProposalDTO);
    }

    /**
     * {@code DELETE  /vote-proposals/:id} : delete the "id" voteProposal.
     *
     * @param id the id of the voteProposalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vote-proposals/{id}")
    public ResponseEntity<Void> deleteVoteProposal(@PathVariable Long id) {
        log.debug("REST request to delete VoteProposal : {}", id);
        voteProposalService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

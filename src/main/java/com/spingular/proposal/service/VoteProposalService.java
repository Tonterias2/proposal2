package com.spingular.proposal.service;

import com.spingular.proposal.service.dto.VoteProposalDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.spingular.proposal.domain.VoteProposal}.
 */
public interface VoteProposalService {

    /**
     * Save a voteProposal.
     *
     * @param voteProposalDTO the entity to save.
     * @return the persisted entity.
     */
    VoteProposalDTO save(VoteProposalDTO voteProposalDTO);

    /**
     * Get all the voteProposals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VoteProposalDTO> findAll(Pageable pageable);


    /**
     * Get the "id" voteProposal.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VoteProposalDTO> findOne(Long id);

    /**
     * Delete the "id" voteProposal.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

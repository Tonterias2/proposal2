package com.spingular.proposal.service;

import com.spingular.proposal.service.dto.ProposalUserDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.spingular.proposal.domain.ProposalUser}.
 */
public interface ProposalUserService {

    /**
     * Save a proposalUser.
     *
     * @param proposalUserDTO the entity to save.
     * @return the persisted entity.
     */
    ProposalUserDTO save(ProposalUserDTO proposalUserDTO);

    /**
     * Get all the proposalUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProposalUserDTO> findAll(Pageable pageable);


    /**
     * Get the "id" proposalUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProposalUserDTO> findOne(Long id);

    /**
     * Delete the "id" proposalUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

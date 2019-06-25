package com.spingular.proposal.service.impl;

import com.spingular.proposal.service.VoteProposalService;
import com.spingular.proposal.domain.VoteProposal;
import com.spingular.proposal.repository.VoteProposalRepository;
import com.spingular.proposal.service.dto.VoteProposalDTO;
import com.spingular.proposal.service.mapper.VoteProposalMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link VoteProposal}.
 */
@Service
@Transactional
public class VoteProposalServiceImpl implements VoteProposalService {

    private final Logger log = LoggerFactory.getLogger(VoteProposalServiceImpl.class);

    private final VoteProposalRepository voteProposalRepository;

    private final VoteProposalMapper voteProposalMapper;

    public VoteProposalServiceImpl(VoteProposalRepository voteProposalRepository, VoteProposalMapper voteProposalMapper) {
        this.voteProposalRepository = voteProposalRepository;
        this.voteProposalMapper = voteProposalMapper;
    }

    /**
     * Save a voteProposal.
     *
     * @param voteProposalDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public VoteProposalDTO save(VoteProposalDTO voteProposalDTO) {
        log.debug("Request to save VoteProposal : {}", voteProposalDTO);
        VoteProposal voteProposal = voteProposalMapper.toEntity(voteProposalDTO);
        voteProposal = voteProposalRepository.save(voteProposal);
        return voteProposalMapper.toDto(voteProposal);
    }

    /**
     * Get all the voteProposals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VoteProposalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VoteProposals");
        return voteProposalRepository.findAll(pageable)
            .map(voteProposalMapper::toDto);
    }


    /**
     * Get one voteProposal by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<VoteProposalDTO> findOne(Long id) {
        log.debug("Request to get VoteProposal : {}", id);
        return voteProposalRepository.findById(id)
            .map(voteProposalMapper::toDto);
    }

    /**
     * Delete the voteProposal by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete VoteProposal : {}", id);
        voteProposalRepository.deleteById(id);
    }
}

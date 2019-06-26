package com.spingular.proposal.service.impl;

import com.spingular.proposal.service.ProposalUserService;
import com.spingular.proposal.domain.ProposalUser;
import com.spingular.proposal.repository.ProposalUserRepository;
import com.spingular.proposal.service.dto.ProposalUserDTO;
import com.spingular.proposal.service.mapper.ProposalUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ProposalUser}.
 */
@Service
@Transactional
public class ProposalUserServiceImpl implements ProposalUserService {

    private final Logger log = LoggerFactory.getLogger(ProposalUserServiceImpl.class);

    private final ProposalUserRepository proposalUserRepository;

    private final ProposalUserMapper proposalUserMapper;

    public ProposalUserServiceImpl(ProposalUserRepository proposalUserRepository, ProposalUserMapper proposalUserMapper) {
        this.proposalUserRepository = proposalUserRepository;
        this.proposalUserMapper = proposalUserMapper;
    }

    /**
     * Save a proposalUser.
     *
     * @param proposalUserDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ProposalUserDTO save(ProposalUserDTO proposalUserDTO) {
        log.debug("Request to save ProposalUser : {}", proposalUserDTO);
        if (proposalUserDTO.getAssignedVotesPoints().equals(null)) {
        	proposalUserDTO.setAssignedVotesPoints(100L);//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        }
        ProposalUser proposalUser = proposalUserMapper.toEntity(proposalUserDTO);
        proposalUser = proposalUserRepository.save(proposalUser);
        return proposalUserMapper.toDto(proposalUser);
    }

    /**
     * Get all the proposalUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProposalUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProposalUsers");
        return proposalUserRepository.findAll(pageable)
            .map(proposalUserMapper::toDto);
    }


    /**
     * Get one proposalUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ProposalUserDTO> findOne(Long id) {
        log.debug("Request to get ProposalUser : {}", id);
        return proposalUserRepository.findById(id)
            .map(proposalUserMapper::toDto);
    }

    /**
     * Delete the proposalUser by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProposalUser : {}", id);
        proposalUserRepository.deleteById(id);
    }
}

package com.spingular.proposal.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.spingular.proposal.domain.ProposalUser;
import com.spingular.proposal.domain.*; // for static metamodels
import com.spingular.proposal.repository.ProposalUserRepository;
import com.spingular.proposal.service.dto.ProposalUserCriteria;
import com.spingular.proposal.service.dto.ProposalUserDTO;
import com.spingular.proposal.service.mapper.ProposalUserMapper;

/**
 * Service for executing complex queries for {@link ProposalUser} entities in the database.
 * The main input is a {@link ProposalUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProposalUserDTO} or a {@link Page} of {@link ProposalUserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProposalUserQueryService extends QueryService<ProposalUser> {

    private final Logger log = LoggerFactory.getLogger(ProposalUserQueryService.class);

    private final ProposalUserRepository proposalUserRepository;

    private final ProposalUserMapper proposalUserMapper;

    public ProposalUserQueryService(ProposalUserRepository proposalUserRepository, ProposalUserMapper proposalUserMapper) {
        this.proposalUserRepository = proposalUserRepository;
        this.proposalUserMapper = proposalUserMapper;
    }

    /**
     * Return a {@link List} of {@link ProposalUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProposalUserDTO> findByCriteria(ProposalUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProposalUser> specification = createSpecification(criteria);
        return proposalUserMapper.toDto(proposalUserRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProposalUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProposalUserDTO> findByCriteria(ProposalUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProposalUser> specification = createSpecification(criteria);
        return proposalUserRepository.findAll(specification, page)
            .map(proposalUserMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProposalUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProposalUser> specification = createSpecification(criteria);
        return proposalUserRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    private Specification<ProposalUser> createSpecification(ProposalUserCriteria criteria) {
        Specification<ProposalUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ProposalUser_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), ProposalUser_.creationDate));
            }
            if (criteria.getAssignedVotesPoints() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAssignedVotesPoints(), ProposalUser_.assignedVotesPoints));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(ProposalUser_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getProposalId() != null) {
                specification = specification.and(buildSpecification(criteria.getProposalId(),
                    root -> root.join(ProposalUser_.proposals, JoinType.LEFT).get(Proposal_.id)));
            }
            if (criteria.getVoteProposalId() != null) {
                specification = specification.and(buildSpecification(criteria.getVoteProposalId(),
                    root -> root.join(ProposalUser_.voteProposals, JoinType.LEFT).get(VoteProposal_.id)));
            }
        }
        return specification;
    }
}

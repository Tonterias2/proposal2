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

import com.spingular.proposal.domain.Proposal;
import com.spingular.proposal.domain.*; // for static metamodels
import com.spingular.proposal.repository.ProposalRepository;
import com.spingular.proposal.service.dto.ProposalCriteria;
import com.spingular.proposal.service.dto.ProposalDTO;
import com.spingular.proposal.service.mapper.ProposalMapper;

/**
 * Service for executing complex queries for {@link Proposal} entities in the database.
 * The main input is a {@link ProposalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProposalDTO} or a {@link Page} of {@link ProposalDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProposalQueryService extends QueryService<Proposal> {

    private final Logger log = LoggerFactory.getLogger(ProposalQueryService.class);

    private final ProposalRepository proposalRepository;

    private final ProposalMapper proposalMapper;

    public ProposalQueryService(ProposalRepository proposalRepository, ProposalMapper proposalMapper) {
        this.proposalRepository = proposalRepository;
        this.proposalMapper = proposalMapper;
    }

    /**
     * Return a {@link List} of {@link ProposalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProposalDTO> findByCriteria(ProposalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Proposal> specification = createSpecification(criteria);
        return proposalMapper.toDto(proposalRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProposalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProposalDTO> findByCriteria(ProposalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Proposal> specification = createSpecification(criteria);
        return proposalRepository.findAll(specification, page)
            .map(proposalMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProposalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Proposal> specification = createSpecification(criteria);
        return proposalRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    private Specification<Proposal> createSpecification(ProposalCriteria criteria) {
        Specification<Proposal> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Proposal_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Proposal_.creationDate));
            }
            if (criteria.getProposalName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProposalName(), Proposal_.proposalName));
            }
            if (criteria.getProposalType() != null) {
                specification = specification.and(buildSpecification(criteria.getProposalType(), Proposal_.proposalType));
            }
            if (criteria.getProposalRole() != null) {
                specification = specification.and(buildSpecification(criteria.getProposalRole(), Proposal_.proposalRole));
            }
            if (criteria.getReleaseDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReleaseDate(), Proposal_.releaseDate));
            }
            if (criteria.getProposalVotes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProposalVotes(), Proposal_.proposalVotes));
            }
            if (criteria.getProposalUserVotes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProposalUserVotes(), Proposal_.proposalUserVotes));
            }
            if (criteria.getVoteProposalId() != null) {
                specification = specification.and(buildSpecification(criteria.getVoteProposalId(),
                    root -> root.join(Proposal_.voteProposals, JoinType.LEFT).get(VoteProposal_.id)));
            }
            if (criteria.getProposalUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getProposalUserId(),
                    root -> root.join(Proposal_.proposalUser, JoinType.LEFT).get(ProposalUser_.id)));
            }
            if (criteria.getPostId() != null) {
                specification = specification.and(buildSpecification(criteria.getPostId(),
                    root -> root.join(Proposal_.post, JoinType.LEFT).get(Post_.id)));
            }
        }
        return specification;
    }
}
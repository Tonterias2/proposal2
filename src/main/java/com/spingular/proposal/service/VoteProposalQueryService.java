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

import com.spingular.proposal.domain.VoteProposal;
import com.spingular.proposal.domain.*; // for static metamodels
import com.spingular.proposal.repository.VoteProposalRepository;
import com.spingular.proposal.service.dto.VoteProposalCriteria;
import com.spingular.proposal.service.dto.VoteProposalDTO;
import com.spingular.proposal.service.mapper.VoteProposalMapper;

/**
 * Service for executing complex queries for {@link VoteProposal} entities in the database.
 * The main input is a {@link VoteProposalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VoteProposalDTO} or a {@link Page} of {@link VoteProposalDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VoteProposalQueryService extends QueryService<VoteProposal> {

    private final Logger log = LoggerFactory.getLogger(VoteProposalQueryService.class);

    private final VoteProposalRepository voteProposalRepository;

    private final VoteProposalMapper voteProposalMapper;

    public VoteProposalQueryService(VoteProposalRepository voteProposalRepository, VoteProposalMapper voteProposalMapper) {
        this.voteProposalRepository = voteProposalRepository;
        this.voteProposalMapper = voteProposalMapper;
    }

    /**
     * Return a {@link List} of {@link VoteProposalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VoteProposalDTO> findByCriteria(VoteProposalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<VoteProposal> specification = createSpecification(criteria);
        return voteProposalMapper.toDto(voteProposalRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VoteProposalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VoteProposalDTO> findByCriteria(VoteProposalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<VoteProposal> specification = createSpecification(criteria);
        return voteProposalRepository.findAll(specification, page)
            .map(voteProposalMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VoteProposalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<VoteProposal> specification = createSpecification(criteria);
        return voteProposalRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    private Specification<VoteProposal> createSpecification(VoteProposalCriteria criteria) {
        Specification<VoteProposal> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), VoteProposal_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), VoteProposal_.creationDate));
            }
            if (criteria.getVotePoints() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVotePoints(), VoteProposal_.votePoints));
            }
            if (criteria.getProposalId() != null) {
                specification = specification.and(buildSpecification(criteria.getProposalId(),
                    root -> root.join(VoteProposal_.proposal, JoinType.LEFT).get(Proposal_.id)));
            }
            if (criteria.getProposalUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getProposalUserId(),
                    root -> root.join(VoteProposal_.proposalUser, JoinType.LEFT).get(ProposalUser_.id)));
            }
        }
        return specification;
    }
}

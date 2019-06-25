package com.spingular.proposal.repository;

import com.spingular.proposal.domain.Proposal;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Proposal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Long>, JpaSpecificationExecutor<Proposal> {

}

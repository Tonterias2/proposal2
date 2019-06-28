package com.spingular.proposal.repository;

import com.spingular.proposal.domain.VoteProposal;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the VoteProposal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoteProposalRepository extends JpaRepository<VoteProposal, Long>, JpaSpecificationExecutor<VoteProposal> {

}

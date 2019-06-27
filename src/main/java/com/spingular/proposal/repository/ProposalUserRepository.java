package com.spingular.proposal.repository;

import com.spingular.proposal.domain.ProposalUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ProposalUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProposalUserRepository extends JpaRepository<ProposalUser, Long>, JpaSpecificationExecutor<ProposalUser> {

}

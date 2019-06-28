package com.spingular.proposal.service.mapper;

import com.spingular.proposal.domain.*;
import com.spingular.proposal.service.dto.ProposalUserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProposalUser} and its DTO {@link ProposalUserDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ProposalUserMapper extends EntityMapper<ProposalUserDTO, ProposalUser> {

    @Mapping(source = "user.id", target = "userId")
    ProposalUserDTO toDto(ProposalUser proposalUser);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "proposals", ignore = true)
    @Mapping(target = "removeProposal", ignore = true)
    @Mapping(target = "voteProposals", ignore = true)
    @Mapping(target = "removeVoteProposal", ignore = true)
    ProposalUser toEntity(ProposalUserDTO proposalUserDTO);

    default ProposalUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProposalUser proposalUser = new ProposalUser();
        proposalUser.setId(id);
        return proposalUser;
    }
}

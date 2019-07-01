package com.spingular.proposal.service.mapper;

import com.spingular.proposal.domain.*;
import com.spingular.proposal.service.dto.VoteProposalDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link VoteProposal} and its DTO {@link VoteProposalDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProposalMapper.class, ProfileMapper.class})
public interface VoteProposalMapper extends EntityMapper<VoteProposalDTO, VoteProposal> {

    @Mapping(source = "proposal.id", target = "proposalId")
    @Mapping(source = "profile.id", target = "profileId")
    VoteProposalDTO toDto(VoteProposal voteProposal);

    @Mapping(source = "proposalId", target = "proposal")
    @Mapping(source = "profileId", target = "profile")
    VoteProposal toEntity(VoteProposalDTO voteProposalDTO);

    default VoteProposal fromId(Long id) {
        if (id == null) {
            return null;
        }
        VoteProposal voteProposal = new VoteProposal();
        voteProposal.setId(id);
        return voteProposal;
    }
}

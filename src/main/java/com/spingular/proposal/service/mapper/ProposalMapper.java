package com.spingular.proposal.service.mapper;

import com.spingular.proposal.domain.*;
import com.spingular.proposal.service.dto.ProposalDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Proposal} and its DTO {@link ProposalDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class, PostMapper.class})
public interface ProposalMapper extends EntityMapper<ProposalDTO, Proposal> {

    @Mapping(source = "profile.id", target = "profileId")
    @Mapping(source = "post.id", target = "postId")
    ProposalDTO toDto(Proposal proposal);

    @Mapping(target = "voteProposals", ignore = true)
    @Mapping(target = "removeVoteProposal", ignore = true)
    @Mapping(source = "profileId", target = "profile")
    @Mapping(source = "postId", target = "post")
    Proposal toEntity(ProposalDTO proposalDTO);

    default Proposal fromId(Long id) {
        if (id == null) {
            return null;
        }
        Proposal proposal = new Proposal();
        proposal.setId(id);
        return proposal;
    }
}

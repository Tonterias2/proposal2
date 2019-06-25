package com.spingular.proposal.service.mapper;

import com.spingular.proposal.domain.*;
import com.spingular.proposal.service.dto.PostDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Post} and its DTO {@link PostDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PostMapper extends EntityMapper<PostDTO, Post> {


    @Mapping(target = "proposals", ignore = true)
    @Mapping(target = "removeProposal", ignore = true)
    Post toEntity(PostDTO postDTO);

    default Post fromId(Long id) {
        if (id == null) {
            return null;
        }
        Post post = new Post();
        post.setId(id);
        return post;
    }
}

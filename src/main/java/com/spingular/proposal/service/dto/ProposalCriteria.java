package com.spingular.proposal.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.spingular.proposal.domain.enumeration.ProposalType;
import com.spingular.proposal.domain.enumeration.ProposalRole;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.spingular.proposal.domain.Proposal} entity. This class is used
 * in {@link com.spingular.proposal.web.rest.ProposalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /proposals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProposalCriteria implements Serializable, Criteria {
    /**
     * Class for filtering ProposalType
     */
    public static class ProposalTypeFilter extends Filter<ProposalType> {

        public ProposalTypeFilter() {
        }

        public ProposalTypeFilter(ProposalTypeFilter filter) {
            super(filter);
        }

        @Override
        public ProposalTypeFilter copy() {
            return new ProposalTypeFilter(this);
        }

    }
    /**
     * Class for filtering ProposalRole
     */
    public static class ProposalRoleFilter extends Filter<ProposalRole> {

        public ProposalRoleFilter() {
        }

        public ProposalRoleFilter(ProposalRoleFilter filter) {
            super(filter);
        }

        @Override
        public ProposalRoleFilter copy() {
            return new ProposalRoleFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private StringFilter proposalName;

    private ProposalTypeFilter proposalType;

    private ProposalRoleFilter proposalRole;

    private InstantFilter releaseDate;

    private LongFilter proposalVotes;

    private LongFilter proposalUserVotes;

    private LongFilter voteProposalId;

    private LongFilter proposalUserId;

    private LongFilter postId;

    public ProposalCriteria(){
    }

    public ProposalCriteria(ProposalCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.proposalName = other.proposalName == null ? null : other.proposalName.copy();
        this.proposalType = other.proposalType == null ? null : other.proposalType.copy();
        this.proposalRole = other.proposalRole == null ? null : other.proposalRole.copy();
        this.releaseDate = other.releaseDate == null ? null : other.releaseDate.copy();
        this.proposalVotes = other.proposalVotes == null ? null : other.proposalVotes.copy();
        this.proposalUserVotes = other.proposalUserVotes == null ? null : other.proposalUserVotes.copy();
        this.voteProposalId = other.voteProposalId == null ? null : other.voteProposalId.copy();
        this.proposalUserId = other.proposalUserId == null ? null : other.proposalUserId.copy();
        this.postId = other.postId == null ? null : other.postId.copy();
    }

    @Override
    public ProposalCriteria copy() {
        return new ProposalCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(InstantFilter creationDate) {
        this.creationDate = creationDate;
    }

    public StringFilter getProposalName() {
        return proposalName;
    }

    public void setProposalName(StringFilter proposalName) {
        this.proposalName = proposalName;
    }

    public ProposalTypeFilter getProposalType() {
        return proposalType;
    }

    public void setProposalType(ProposalTypeFilter proposalType) {
        this.proposalType = proposalType;
    }

    public ProposalRoleFilter getProposalRole() {
        return proposalRole;
    }

    public void setProposalRole(ProposalRoleFilter proposalRole) {
        this.proposalRole = proposalRole;
    }

    public InstantFilter getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(InstantFilter releaseDate) {
        this.releaseDate = releaseDate;
    }

    public LongFilter getProposalVotes() {
        return proposalVotes;
    }

    public void setProposalVotes(LongFilter proposalVotes) {
        this.proposalVotes = proposalVotes;
    }

    public LongFilter getProposalUserVotes() {
        return proposalUserVotes;
    }

    public void setProposalUserVotes(LongFilter proposalUserVotes) {
        this.proposalUserVotes = proposalUserVotes;
    }

    public LongFilter getVoteProposalId() {
        return voteProposalId;
    }

    public void setVoteProposalId(LongFilter voteProposalId) {
        this.voteProposalId = voteProposalId;
    }

    public LongFilter getProposalUserId() {
        return proposalUserId;
    }

    public void setProposalUserId(LongFilter proposalUserId) {
        this.proposalUserId = proposalUserId;
    }

    public LongFilter getPostId() {
        return postId;
    }

    public void setPostId(LongFilter postId) {
        this.postId = postId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProposalCriteria that = (ProposalCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(proposalName, that.proposalName) &&
            Objects.equals(proposalType, that.proposalType) &&
            Objects.equals(proposalRole, that.proposalRole) &&
            Objects.equals(releaseDate, that.releaseDate) &&
            Objects.equals(proposalVotes, that.proposalVotes) &&
            Objects.equals(proposalUserVotes, that.proposalUserVotes) &&
            Objects.equals(voteProposalId, that.voteProposalId) &&
            Objects.equals(proposalUserId, that.proposalUserId) &&
            Objects.equals(postId, that.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        creationDate,
        proposalName,
        proposalType,
        proposalRole,
        releaseDate,
        proposalVotes,
        proposalUserVotes,
        voteProposalId,
        proposalUserId,
        postId
        );
    }

    @Override
    public String toString() {
        return "ProposalCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
                (proposalName != null ? "proposalName=" + proposalName + ", " : "") +
                (proposalType != null ? "proposalType=" + proposalType + ", " : "") +
                (proposalRole != null ? "proposalRole=" + proposalRole + ", " : "") +
                (releaseDate != null ? "releaseDate=" + releaseDate + ", " : "") +
                (proposalVotes != null ? "proposalVotes=" + proposalVotes + ", " : "") +
                (proposalUserVotes != null ? "proposalUserVotes=" + proposalUserVotes + ", " : "") +
                (voteProposalId != null ? "voteProposalId=" + voteProposalId + ", " : "") +
                (proposalUserId != null ? "proposalUserId=" + proposalUserId + ", " : "") +
                (postId != null ? "postId=" + postId + ", " : "") +
            "}";
    }

}

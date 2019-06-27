package com.spingular.proposal.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.spingular.proposal.domain.ProposalUser} entity. This class is used
 * in {@link com.spingular.proposal.web.rest.ProposalUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /proposal-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProposalUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private LongFilter assignedVotesPoints;

    private LongFilter userId;

    private LongFilter proposalId;

    private LongFilter voteProposalId;

    public ProposalUserCriteria(){
    }

    public ProposalUserCriteria(ProposalUserCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.assignedVotesPoints = other.assignedVotesPoints == null ? null : other.assignedVotesPoints.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.proposalId = other.proposalId == null ? null : other.proposalId.copy();
        this.voteProposalId = other.voteProposalId == null ? null : other.voteProposalId.copy();
    }

    @Override
    public ProposalUserCriteria copy() {
        return new ProposalUserCriteria(this);
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

    public LongFilter getAssignedVotesPoints() {
        return assignedVotesPoints;
    }

    public void setAssignedVotesPoints(LongFilter assignedVotesPoints) {
        this.assignedVotesPoints = assignedVotesPoints;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getProposalId() {
        return proposalId;
    }

    public void setProposalId(LongFilter proposalId) {
        this.proposalId = proposalId;
    }

    public LongFilter getVoteProposalId() {
        return voteProposalId;
    }

    public void setVoteProposalId(LongFilter voteProposalId) {
        this.voteProposalId = voteProposalId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProposalUserCriteria that = (ProposalUserCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(assignedVotesPoints, that.assignedVotesPoints) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(proposalId, that.proposalId) &&
            Objects.equals(voteProposalId, that.voteProposalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        creationDate,
        assignedVotesPoints,
        userId,
        proposalId,
        voteProposalId
        );
    }

    @Override
    public String toString() {
        return "ProposalUserCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
                (assignedVotesPoints != null ? "assignedVotesPoints=" + assignedVotesPoints + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (proposalId != null ? "proposalId=" + proposalId + ", " : "") +
                (voteProposalId != null ? "voteProposalId=" + voteProposalId + ", " : "") +
            "}";
    }

}

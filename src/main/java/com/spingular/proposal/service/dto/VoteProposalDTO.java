package com.spingular.proposal.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.spingular.proposal.domain.VoteProposal} entity.
 */
public class VoteProposalDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant creationDate;

    @NotNull
    private Long votePoints;


    private Long proposalId;

    private Long proposalUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Long getVotePoints() {
        return votePoints;
    }

    public void setVotePoints(Long votePoints) {
        this.votePoints = votePoints;
    }

    public Long getProposalId() {
        return proposalId;
    }

    public void setProposalId(Long proposalId) {
        this.proposalId = proposalId;
    }

    public Long getProposalUserId() {
        return proposalUserId;
    }

    public void setProposalUserId(Long proposalUserId) {
        this.proposalUserId = proposalUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VoteProposalDTO voteProposalDTO = (VoteProposalDTO) o;
        if (voteProposalDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), voteProposalDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VoteProposalDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", votePoints=" + getVotePoints() +
            ", proposal=" + getProposalId() +
            ", proposalUser=" + getProposalUserId() +
            "}";
    }
}

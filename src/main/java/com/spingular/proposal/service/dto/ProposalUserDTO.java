package com.spingular.proposal.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.spingular.proposal.domain.ProposalUser} entity.
 */
public class ProposalUserDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant creationDate;

    private Long assignedVotesPoints;


    private Long userId;

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

    public Long getAssignedVotesPoints() {
        return assignedVotesPoints;
    }

    public void setAssignedVotesPoints(Long assignedVotesPoints) {
        this.assignedVotesPoints = assignedVotesPoints;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProposalUserDTO proposalUserDTO = (ProposalUserDTO) o;
        if (proposalUserDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), proposalUserDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProposalUserDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", assignedVotesPoints=" + getAssignedVotesPoints() +
            ", user=" + getUserId() +
            "}";
    }
}
